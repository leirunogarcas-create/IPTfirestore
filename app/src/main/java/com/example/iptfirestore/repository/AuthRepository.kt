package com.example.iptfirestore.repository

import com.example.iptfirestore.model.FirestoreNote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Authentication failed: User is null."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                if (name.isNotBlank()) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user.updateProfile(profileUpdates).await()
                }
                
                // Also store user profile in Firestore 'users' collection
                val userMap = hashMapOf(
                    "uid" to user.uid,
                    "name" to name,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )
                try {
                    firestore.collection("users").document(user.uid).set(userMap).await()
                } catch (_: Exception) {
                    // Non-blocking if Firestore rules block write without auth propagation
                }
                
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed: User creation failed."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        try {
            auth.signOut()
        } catch (_: Exception) {
        }
    }

    suspend fun addNote(title: String, content: String): Result<String> {
        val user = currentUser ?: return Result.failure(Exception("User not authenticated."))
        return try {
            val docRef = firestore.collection("notes").document()
            val note = hashMapOf(
                "id" to docRef.id,
                "title" to title,
                "content" to content,
                "userEmail" to (user.email ?: "Unknown"),
                "userId" to user.uid,
                "timestamp" to System.currentTimeMillis()
            )
            docRef.set(note).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            firestore.collection("notes").document(noteId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getNotesFlow(): Flow<List<FirestoreNote>> = callbackFlow {
        val listenerRegistration = firestore.collection("notes")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val notes = snapshot?.documents?.mapNotNull { doc ->
                    val id = doc.getString("id") ?: doc.id
                    val title = doc.getString("title") ?: ""
                    val content = doc.getString("content") ?: ""
                    val userEmail = doc.getString("userEmail") ?: ""
                    val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    FirestoreNote(id, title, content, userEmail, timestamp)
                } ?: emptyList()

                trySend(notes)
            }

        awaitClose { listenerRegistration.remove() }
    }
}
