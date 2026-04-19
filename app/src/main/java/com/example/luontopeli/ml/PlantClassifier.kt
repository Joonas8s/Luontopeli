package com.example.luontopeli.ml

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * ML Kit -pohjainen kasvin tunnistaja (on-device Image Labeling).
 * Käyttää Google ML Kit:n paikallista kuvatunnistusta.
 */
class PlantClassifier {

    /** ML Kit Image Labeler -instanssi. */
    val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.5f)
            .build()
    )

    private val natureKeywords = setOf(
        "plant", "flower", "tree", "shrub", "leaf", "fern", "moss",
        "mushroom", "fungus", "grass", "herb", "bush", "berry",
        "pine", "birch", "spruce", "algae", "lichen", "bark",
        "nature", "forest", "woodland", "botanical", "flora"
    )

    /**
     * Analysoi kuvan ja tunnistaa siitä luontokohteet (valokuvan ottamisen jälkeen).
     */
    suspend fun classify(imageUri: Uri, context: Context): ClassificationResult {
        return suspendCancellableCoroutine { continuation ->
            try {
                val inputImage = InputImage.fromFilePath(context, imageUri)
                processInputImage(inputImage)
                    .addOnSuccessListener { labels ->
                        val natureLabels = filterNatureLabels(labels)
                        val result = if (natureLabels.isNotEmpty()) {
                            val best = natureLabels.maxByOrNull { it.confidence }!!
                            ClassificationResult.Success(
                                label = best.text,
                                confidence = best.confidence,
                                allLabels = labels.take(5)
                            )
                        } else {
                            ClassificationResult.NotNature(allLabels = labels.take(3))
                        }
                        continuation.resume(result)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    /**
     * Suorittaa ML Kit -tunnistuksen annetulle InputImage-kuvalle.
     */
    fun processInputImage(image: InputImage): Task<List<ImageLabel>> {
        return labeler.process(image)
    }

    /**
     * Suodattaa luontoon liittyvät merkinnät.
     */
    fun filterNatureLabels(labels: List<ImageLabel>): List<ImageLabel> {
        return labels.filter { label ->
            natureKeywords.any { keyword ->
                label.text.contains(keyword, ignoreCase = true)
            }
        }
    }

    /** Vapauttaa ML Kit -resurssit. */
    fun close() {
        labeler.close()
    }
}
