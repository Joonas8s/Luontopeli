package com.example.luontopeli.ml

import com.google.mlkit.vision.label.ImageLabel

/**
 * Sealed class kuvatunnistuksen tulokselle.
 */
sealed class ClassificationResult {
    /**
     * Onnistunut tunnistus – kuva sisältää luontokohteen.
     */
    data class Success(
        val label: String,
        val confidence: Float,
        val allLabels: List<ImageLabel>
    ) : ClassificationResult()

    /**
     * Kuva ei sisällä luontokohteita.
     */
    data class NotNature(
        val allLabels: List<ImageLabel>
    ) : ClassificationResult()

    /**
     * Tunnistus epäonnistui.
     */
    data class Error(val message: String) : ClassificationResult()
}
