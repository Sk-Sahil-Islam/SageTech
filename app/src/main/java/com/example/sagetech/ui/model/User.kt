package com.example.sagetech.ui.model

import androidx.compose.ui.graphics.painter.Painter

data class User(
    val rank: Int,
    val username: String,
    val scroll: Long,
    val pfp: Painter
)

val bullyList = listOf(
    "Your thumb is training for a marathon it never signed up for.",
    "Still no end in sight? Neither is your self-control.",
    "Bet you don’t even remember what you started looking for.",
    "If scrolling burned calories, you’d be shredded by now.",
    "Congratulations. You’ve earned the ‘Digital Worm’ badge.",
    "The void is proud of you.",
    "Ever considered touching grass?",
    "You're officially longer than the Titanic. Let that sink in.",
    "WARNING: Excessive scrolling may cause thumb existential crisis.",
    "You've scrolled past the Tower of Babel. Language has failed you.",
    "This could’ve been a nap. Or a hug. Or literally anything else.",
    "Your ancestors are disappointed.",
    "You're now legally part of the algorithm.",
    "Your attention span called. It’s on life support.",
    "Remember books? People used to scroll those manually.",
    "You’ve scrolled so much, even doom is tired."
)

val scrollHeightMap = hashMapOf(
    1 to "Banana",
    2 to "Cat",
    4 to "Human leg",
    6 to "Human height",
    9 to "Alligator",
    12 to "Kayak",
    15 to "Giraffe neck",
    20 to "School bus",
    25 to "T-Rex",
    30 to "Train Car (1 bogie)",
    35 to "London Double-Decker Bus",
    40 to "Shipping Container",
    50 to "Semi Truck Trailer",
    60 to "Humpback Whale",
    70 to "Boeing 737 Wing",
    80 to "Brontosaurus (est.)",
    90 to "Baseball Diamond Baseline",
    100 to "Giant Redwood Tree (small)",
    120 to "Telephone Pole Stack (3x)",
    150 to "Blue Whale (full length)",
    180 to "Apollo Saturn V Rocket",
    200 to "Statue of Liberty (to torch)",
    250 to "Suspension Bridge Span",
    275 to "Football Field Sideline",
    300 to "The Eiffel Tower Base Width"
)
