package com.shishusneh.app.utils

import com.shishusneh.app.data.entity.FeedingTipEntity
import com.shishusneh.app.data.entity.MilestoneEntity
import com.shishusneh.app.data.entity.VaccinationEntity

object SeedData {

    fun vaccinations(babyId: Long, dobMillis: Long): List<VaccinationEntity> = listOf(
        Triple("BCG", "Tuberculosis", 0),
        Triple("OPV-0", "Polio", 0),
        Triple("Hepatitis B Birth Dose", "Hepatitis B", 0),
        Triple("Pentavalent-1", "Diphtheria, Pertussis, Tetanus, Hepatitis B, Hib", 42),
        Triple("OPV-1", "Polio", 42),
        Triple("Rotavirus-1", "Rotavirus diarrhoea", 42),
        Triple("PCV-1", "Pneumococcal disease", 42),
        Triple("Pentavalent-2", "Diphtheria, Pertussis, Tetanus, Hepatitis B, Hib", 70),
        Triple("OPV-2", "Polio", 70),
        Triple("Rotavirus-2", "Rotavirus diarrhoea", 70),
        Triple("PCV-2", "Pneumococcal disease", 70),
        Triple("Pentavalent-3", "Diphtheria, Pertussis, Tetanus, Hepatitis B, Hib", 98),
        Triple("OPV-3", "Polio", 98),
        Triple("IPV", "Polio", 98),
        Triple("MR-1", "Measles and Rubella", 270),
        Triple("JE-1", "Japanese Encephalitis", 270),
        Triple("PCV Booster", "Pneumococcal disease", 270)
    ).map { (name, disease, days) ->
        VaccinationEntity(
            babyId = babyId,
            name = name,
            diseasePrevented = disease,
            dueDateMillis = DateUtils.addDays(dobMillis, days)
        )
    }

    fun milestones(babyId: Long): List<MilestoneEntity> = listOf(
        Triple("Social smile", "Baby smiles back when spoken to.", 2),
        Triple("Head control", "Holds head steady during tummy time.", 3),
        Triple("Roll over", "Rolls from tummy to back.", 4),
        Triple("Reach and grasp", "Reaches for and grasps nearby toys.", 5),
        Triple("Sits with support", "Sits with minimal support.", 6),
        Triple("Recognizes familiar faces", "Responds to mother and close family.", 6),
        Triple("Crawling attempts", "Starts pushing forward or rocking.", 8),
        Triple("Pulls to stand", "Begins standing with support.", 9),
        Triple("Pincer grasp", "Uses thumb and finger to hold objects.", 10),
        Triple("First words", "Babbles clear syllables like mama or dada.", 11),
        Triple("Cruising", "Walks holding furniture.", 11),
        Triple("First steps", "May stand briefly or take first steps.", 12)
    ).map { (title, description, months) ->
        MilestoneEntity(
            babyId = babyId,
            title = title,
            description = description,
            expectedAgeMonths = months
        )
    }

    fun feedingTips(): List<FeedingTipEntity> = listOf(
        FeedingTipEntity(
            minAgeMonths = 0,
            maxAgeMonths = 6,
            category = "tip",
            titleEn = "Exclusive breastfeeding",
            titleHi = "केवल स्तनपान",
            contentEn = "Feed only breast milk every 2-3 hours. No water, honey, or animal milk is needed.",
            contentHi = "हर 2-3 घंटे में केवल मां का दूध दें। पानी, शहद या जानवर का दूध देने की जरूरत नहीं है।"
        ),
        FeedingTipEntity(
            minAgeMonths = 0,
            maxAgeMonths = 6,
            category = "tip",
            titleEn = "Mother hydration",
            titleHi = "मां की पानी की मात्रा",
            contentEn = "Mother should drink enough water, rest often, and continue regular meals to support milk supply.",
            contentHi = "दूध की मात्रा बनाए रखने के लिए मां पर्याप्त पानी पिए, आराम करे और नियमित भोजन करे।"
        ),
        FeedingTipEntity(
            minAgeMonths = 6,
            maxAgeMonths = 9,
            category = "tip",
            titleEn = "Start semi-solids",
            titleHi = "अर्ध-ठोस भोजन शुरू करें",
            contentEn = "Introduce mashed khichdi, dal water, banana mash, and continue breastfeeding.",
            contentHi = "मसली हुई खिचड़ी, दाल का पानी, केला मैश देना शुरू करें और स्तनपान जारी रखें।"
        ),
        FeedingTipEntity(
            minAgeMonths = 9,
            maxAgeMonths = 12,
            category = "tip",
            titleEn = "Family foods in soft form",
            titleHi = "नरम पारिवारिक भोजन",
            contentEn = "Offer soft family foods, egg yolk, seasonal fruits, and iron-rich foods with hand hygiene.",
            contentHi = "नरम पारिवारिक भोजन, अंडे की जर्दी, मौसमी फल और आयरन युक्त भोजन साफ हाथों से दें।"
        ),
        FeedingTipEntity(
            minAgeMonths = 0,
            maxAgeMonths = 12,
            category = "myth",
            titleEn = "Myth: Honey helps immunity",
            titleHi = "मिथक: शहद से रोग प्रतिरोधक क्षमता बढ़ती है",
            contentEn = "Fact: Honey should be avoided before 1 year because it can be unsafe for infants.",
            contentHi = "तथ्य: 1 साल से पहले शहद नहीं देना चाहिए क्योंकि यह शिशु के लिए असुरक्षित हो सकता है।"
        ),
        FeedingTipEntity(
            minAgeMonths = 0,
            maxAgeMonths = 12,
            category = "myth",
            titleEn = "Myth: Formula is always stronger",
            titleHi = "मिथक: फार्मूला दूध हमेशा बेहतर होता है",
            contentEn = "Fact: Breast milk gives ideal nutrition and antibodies, especially during the first 6 months.",
            contentHi = "तथ्य: खासकर पहले 6 महीनों में मां का दूध सर्वोत्तम पोषण और एंटीबॉडी देता है।"
        )
    )

    fun whoReferenceWeights(): List<Pair<Float, Float>> = listOf(
        0f to 3.2f,
        1f to 4.2f,
        2f to 5.1f,
        3f to 5.8f,
        4f to 6.4f,
        5f to 6.9f,
        6f to 7.3f,
        7f to 7.6f,
        8f to 7.9f,
        9f to 8.2f,
        10f to 8.5f,
        11f to 8.7f,
        12f to 8.9f
    )
}
