package com.app.vibess.ui.screens


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.app.vibess.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    // Проверка статуса аутентификации
    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    // Вход в систему
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // Регистрация нового пользователя
    fun signup(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email ?: "", user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Получаем текущего пользователя
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let {
                        // Используем UID как идентификатор документа в Firestore
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users")
                            .document(it.uid) // UID пользователя из Firebase Authentication
                            .set(user) // Сохраняем данные пользователя
                            .addOnSuccessListener {
                                // Успешно сохранили данные
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                // Ошибка при сохранении данных
                                onFailure("Error saving user data: ${e.message}")
                            }
                    }
                } else {
                    // Ошибка при регистрации в Firebase Authentication
                    onFailure("Registration failed: ${task.exception?.message}")
                }
            }
    }

    // Выход из системы
    fun signout() {
        auth.signOut()
    }

    // Получение информации о текущем пользователе
    fun getCurrentUserInfo(navController: NavController, onUserLoaded: (User?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            // Проверяем, есть ли пользователь в Firestore
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(it.uid)

            // Получаем информацию о пользователе
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    // Передаем данные обратно через коллбек
                    onUserLoaded(user)
                } else {
                    // Если данных нет, перенаправляем на страницу входа
                    navController.navigate("login") // Здесь "login" — это имя маршрута для страницы входа
                    // Передаем null, так как данных нет
                    onUserLoaded(null)
                }
            }
        } ?: run {
            // Если нет текущего пользователя, перенаправляем на страницу входа
            navController.navigate("login")
            onUserLoaded(null)
        }
    }
}
sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}
