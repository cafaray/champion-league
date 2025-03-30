package main.kotlin.com.supplier.championleague.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import org.eclipse.microprofile.config.inject.ConfigProperties
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.FileInputStream
import java.io.IOException

@Singleton
class FirebaseConfig {

    // val firebaseAccountPhonic: String = "phonic-altar-450817-q4-firebase-adminsdk-fbsvc-749719c99c";
    // val firebaseAccountFlutter: String = "fir-flutter-codelab-be8c1-firebase-adminsdk-urvx0-8394bd3a24";

    // val firebaseProjectIdPhonic: String = "343004725643";
    // val firebaseProjectIdFlutter: String = "611293408654";

    @ConfigProperty(name = "firebase.project.id")
    lateinit var firebaseProjectId: String

    @PostConstruct
    fun init () {
        if (FirebaseApp.getApps().isEmpty()) {
            try {

                val credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
                    ?: throw IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS is not set!")
                println("*  credentialsPath: $credentialsPath \n*  projectId: $firebaseProjectId")
                // val firebaseAccount: String = StringBuilder("src/main/resources/")
                //    .append(firebaseAccountPhonic)
                //     .append(".json").toString()
                // val serviceAccount = FileInputStream(firebaseAccount)
                val serviceAccount = FileInputStream(credentialsPath)
                // val firebaseProjectId: String = firebaseProjectIdPhonic
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://"+firebaseProjectId+".firebaseio.com")
                    .build()
                FirebaseApp.initializeApp(options)
                println("✅ Firebase initialized successfully!")
            } catch (e: IOException) {
                throw RuntimeException("❌ Error initializing Firebase: ${e.message}")
            }
        }
    }
}