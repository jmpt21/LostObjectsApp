package mx.tecnm.cdhidalgo.lostobjectsapp.ui.myreports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import mx.tecnm.cdhidalgo.lostobjectsapp.auth

class PageViewModel : ViewModel() {
    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = _index.map {
        if (it == 1) {
            "Mis reportes de Objetos encontrados"
        } else {
            "Mis reportes de Objetos perdidos"
        }
    }

    val objects: LiveData<Task<QuerySnapshot>> = _index.map {
        if (it == 1) {
            FirebaseFirestore.getInstance()
                .collection("FoundObjects")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("email", auth.currentUser?.email)
                //.orderBy("date", Query.Direction.DESCENDING)
                .get()
        } else {
            FirebaseFirestore.getInstance()
                .collection("LostObjects")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("email", auth.currentUser?.email)
                //.orderBy("date", Query.Direction.DESCENDING)
                .get()
        }
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}