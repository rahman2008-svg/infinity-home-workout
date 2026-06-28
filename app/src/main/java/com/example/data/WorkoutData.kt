package com.example.data

data class Exercise(
    val name: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val isDurationBased: Boolean,
    val caloriesPerMinute: Float = 6f,
    val animationId: String // Used to match the Canvas animation type
)

data class WorkoutDay(
    val dayIndex: Int, // 1 to 30
    val title: String,
    val focusArea: String,
    val focusAreaBn: String,
    val durationMinutes: Int,
    val exercises: List<WorkoutExercise>
)

data class WorkoutExercise(
    val exercise: Exercise,
    val targetValue: Int, // Number of reps, or duration in seconds
    val restSeconds: Int = 15
)

object WorkoutData {

    // Over 30 core exercises matching the details
    val exerciseLibrary = mapOf(
        "Jumping Jack" to Exercise(
            "Jumping Jack",
            "Stand with your feet together and your hands at your sides. Jump up while spreading your feet and raising your arms above your head. Jump back to the starting position.",
            "সোজা হয়ে দাঁড়ান। লাফ দিয়ে দুই পা দুই দিকে ছড়িয়ে দিন এবং হাত মাথার ওপরে তুলুন। পুনরায় লাফ দিয়ে আগের অবস্থানে ফিরে আসুন।",
            isDurationBased = true,
            caloriesPerMinute = 8f,
            animationId = "jumping_jack"
        ),
        "Push-up" to Exercise(
            "Push-up",
            "Start in a plank position. Lower your body until your chest nearly touches the floor, keeping your body straight. Push back up to the starting position.",
            "প্লাঙ্ক পজিশনে যান। শরীর সোজা রেখে বুক মাটির কাছাকাছি নামান। এরপর হাত সোজা করে আগের অবস্থায় ফিরে আসুন।",
            isDurationBased = false,
            caloriesPerMinute = 7f,
            animationId = "push_up"
        ),
        "Wide Push-up" to Exercise(
            "Wide Push-up",
            "Perform a push-up with your hands placed wider than shoulder-width apart to target the chest muscles.",
            "হাত কাঁধের চেয়ে বেশি দূরত্বে রেখে সাধারণ পুশ-আপের মতো ব্যায়ামটি করুন। এটি বুকের পেশী গঠনে সাহায্য করে।",
            isDurationBased = false,
            caloriesPerMinute = 7.5f,
            animationId = "wide_push_up"
        ),
        "Diamond Push-up" to Exercise(
            "Diamond Push-up",
            "Place your hands close together forming a diamond shape with your thumbs and index fingers, then perform a push-up.",
            "দুই হাত কাছাকাছি রেখে বৃদ্ধাঙ্গুল এবং তর্জনী দিয়ে ডায়মন্ড বা হীরের মতো আকার তৈরি করুন, তারপর পুশ-আপ দিন।",
            isDurationBased = false,
            caloriesPerMinute = 8f,
            animationId = "diamond_push_up"
        ),
        "Incline Push-up" to Exercise(
            "Incline Push-up",
            "Place your hands on an elevated surface like a bench or step, keeping your body straight while doing push-ups.",
            "কোনো উঁচু জায়গা (যেমন টেবিল বা বেঞ্চ) এর উপর হাত রেখে পুশ-আপ দিন। এটি নতুনদের জন্য সহজ।",
            isDurationBased = false,
            caloriesPerMinute = 6f,
            animationId = "incline_push_up"
        ),
        "Squat" to Exercise(
            "Squat",
            "Stand with feet shoulder-width apart. Lower your hips as if sitting in a chair, keeping your back straight and chest up, then return to standing.",
            "পা কাঁধ বরাবর ফাঁক করে দাঁড়ান। সোজা থেকে চেয়ারে বসার মতো করে নিচে নামুন, হাঁটু যেন পায়ের আঙুল অতিক্রম না করে।",
            isDurationBased = false,
            caloriesPerMinute = 6.5f,
            animationId = "squat"
        ),
        "Wall Sit" to Exercise(
            "Wall Sit",
            "Lean against a wall and slide down until your knees form a 90-degree angle. Hold this position.",
            "দেয়ালের পিঠ ঠেকিয়ে ৯০ ডিগ্রি কোণে বসার ভঙ্গি করুন এবং নির্দিষ্ট সময় পর্যন্ত এই অবস্থানে থাকুন।",
            isDurationBased = true,
            caloriesPerMinute = 5f,
            animationId = "wall_sit"
        ),
        "Lunges" to Exercise(
            "Lunges",
            "Step forward with one leg and lower your hips until both knees are bent at about a 90-degree angle. Step back and repeat with the other leg.",
            "এক পা সামনে বাড়িয়ে দিন এবং হাঁটু ৯০ ডিগ্রি কোণে না বাঁকা পর্যন্ত বডি নিচু করুন। আগের জায়গায় এসে অন্য পা দিয়ে পুনরাবৃত্তি করুন।",
            isDurationBased = false,
            caloriesPerMinute = 6f,
            animationId = "lunges"
        ),
        "High Knees" to Exercise(
            "High Knees",
            "Run in place, lifting your knees as high as possible toward your chest with a fast rhythm.",
            "এক জায়গায় দাঁড়িয়ে দ্রুততার সাথে দৌড়ানোর মতো করে হাঁটু বুকের কাছাকাছি তুলুন।",
            isDurationBased = true,
            caloriesPerMinute = 9f,
            animationId = "high_knees"
        ),
        "Mountain Climber" to Exercise(
            "Mountain Climber",
            "Start in a plank position. Alternately drive your knees in toward your chest as fast as you can.",
            "প্লাঙ্ক পজিশন থেকে দ্রুত একের পর এক হাঁটু বুকের দিকে টেনে আনুন, যেন পাহাড়ে চড়ছেন।",
            isDurationBased = true,
            caloriesPerMinute = 9.5f,
            animationId = "mountain_climber"
        ),
        "Burpee" to Exercise(
            "Burpee",
            "Begin in a standing position, drop into a squat, jump your feet back into a plank, perform a push-up, jump back into a squat, and leap into the air.",
            "দাঁড়ানো অবস্থা থেকে নিচে বসে দুই পা পেছনে ছুড়ে প্লাঙ্ক হন, একটি পুশ-আপ দিয়ে লাফিয়ে পুনরায় বসে ওপরের দিকে লাফ দিন।",
            isDurationBased = false,
            caloriesPerMinute = 11f,
            animationId = "burpee"
        ),
        "Plank" to Exercise(
            "Plank",
            "Keep your body in a straight line from head to heels, supported on your forearms and toes. Keep your core tight.",
            "কনুই এবং পায়ের আঙুলের ওপর ভর দিয়ে শরীর মাটির সমান্তরালে সোজা রাখুন। পেট শক্ত রাখুন।",
            isDurationBased = true,
            caloriesPerMinute = 4.5f,
            animationId = "plank"
        ),
        "Side Plank" to Exercise(
            "Side Plank",
            "Lie on your side, supported on one forearm. Lift your hips so your body forms a straight line. Repeat on the other side.",
            "একপাশে কাৎ হয়ে এক কনুইয়ের ওপর ভর দিয়ে শরীর সোজা ওপরে তুলুন। অন্য পাশেও একইভাবে করুন।",
            isDurationBased = true,
            caloriesPerMinute = 4.5f,
            animationId = "side_plank"
        ),
        "Bicycle Crunch" to Exercise(
            "Bicycle Crunch",
            "Lie on your back, bring your hands behind your head, and bring opposite elbow to opposite knee in a pedaling motion.",
            "চিত হয়ে শুয়ে হাত মাথার পেছনে দিন। সাইকেল চালানোর মতো করে এক হাঁটু অন্য কনুইয়ের সাথে মিলিয়ে ক্রাঞ্চ করুন।",
            isDurationBased = false,
            caloriesPerMinute = 7f,
            animationId = "bicycle_crunch"
        ),
        "Russian Twist" to Exercise(
            "Russian Twist",
            "Sit with knees bent and feet slightly off the ground. Twist your torso from side to side, touching the floor beside you.",
            "হাঁটু সামান্য মুড়ে কোমর থেকে শরীর পেছনের দিকে হেলিয়ে পা ওপরে তুলুন। এরপর হাত দিয়ে শরীরের ডানে ও বামে স্পর্শ করুন।",
            isDurationBased = false,
            caloriesPerMinute = 7f,
            animationId = "russian_twist"
        ),
        "Leg Raise" to Exercise(
            "Leg Raise",
            "Lie on your back, keep your legs straight and lift them up to a 90-degree angle, then slowly lower them without touching the floor.",
            "চিত হয়ে শুয়ে দুই পা সোজা রেখে ৯০ ডিগ্রি ওপরে তুলুন এবং আস্তে আস্তে নিচে নামান (মাটি স্পর্শ না করে)।",
            isDurationBased = false,
            caloriesPerMinute = 5f,
            animationId = "leg_raise"
        ),
        "Cobra Stretch" to Exercise(
            "Cobra Stretch",
            "Lie on your stomach, place hands under shoulders, and push your chest up while keeping hips on the floor.",
            "উপুড় হয়ে শুয়ে দুই হাতের তালুর ওপর ভর দিয়ে বুক ওপরের দিকে তুলুন, কোমর মাটিতেই থাকবে। বুক ও পেট প্রসারিত করুন।",
            isDurationBased = true,
            caloriesPerMinute = 3f,
            animationId = "cobra_stretch"
        ),
        "Child Pose" to Exercise(
            "Child Pose",
            "Kneel on the floor, sit back on your heels, bend forward and extend your arms on the floor in front of you.",
            "হাঁটু গেড়ে গোড়ালির ওপর বসুন, এরপর সামনে ঝুঁকে হাত দুটি মাটির সাথে সোজা মেলে মাথা নিচু করুন। শরীর রিল্যাক্স রাখুন।",
            isDurationBased = true,
            caloriesPerMinute = 2.5f,
            animationId = "child_pose"
        ),
        "Arm Circle" to Exercise(
            "Arm Circle",
            "Stand with arms extended straight out to the sides. Rotate them in small, controlled circles.",
            "সোজা হয়ে দাঁড়িয়ে দুই হাত দুই পাশে প্রসারিত করুন এবং ছোট বৃত্তাকারে হাত ঘুরাতে থাকুন।",
            isDurationBased = true,
            caloriesPerMinute = 4f,
            animationId = "arm_circle"
        ),
        "Triceps Dip" to Exercise(
            "Triceps Dip",
            "Place hands on the edge of a chair or bench behind you. Lower your hips by bending your elbows, then push back up.",
            "পিছনে রাখা একটি চেয়ার বা বেঞ্চের কোনায় হাত রেখে কোমর নিচে নামান ও কনুই বাঁকিয়ে আবার ওপরে তুলুন।",
            isDurationBased = false,
            caloriesPerMinute = 6f,
            animationId = "triceps_dip"
        ),
        "Superman" to Exercise(
            "Superman",
            "Lie face down, extend your arms forward and legs straight. Lift arms, chest, and legs off the floor simultaneously.",
            "উপুড় হয়ে শুয়ে হাত সামনে মেলুন। একই সাথে দুই হাত, বুক এবং পা ওপরের দিকে তুলুন এবং হোল্ড করুন।",
            isDurationBased = false,
            caloriesPerMinute = 5.5f,
            animationId = "superman"
        ),
        "Glute Bridge" to Exercise(
            "Glute Bridge",
            "Lie on your back with knees bent and feet flat. Lift your hips until your body forms a straight line from knees to shoulders.",
            "চিত হয়ে শুয়ে হাঁটু ভাঁজ করুন। এরপর কোমর ও নিতম্ব ওপরের দিকে তুলুন যাতে হাঁটু থেকে কাঁধ পর্যন্ত শরীর সোজা থাকে।",
            isDurationBased = false,
            caloriesPerMinute = 5f,
            animationId = "glute_bridge"
        ),
        "Heel Touch" to Exercise(
            "Heel Touch",
            "Lie on your back, knees bent, feet flat. Reach alternately with each hand to touch your corresponding heel.",
            "চিত হয়ে শুয়ে হাঁটু ভাঁজ করুন। কাঁধ সামান্য তুলে ডানে ও বামে কাত হয়ে হাত দিয়ে পায়ের গোড়ালি স্পর্শ করার চেষ্টা করুন।",
            isDurationBased = false,
            caloriesPerMinute = 6f,
            animationId = "heel_touch"
        ),
        "Crunch" to Exercise(
            "Crunch",
            "Lie on your back with knees bent. Contract your abs to lift your head and shoulders off the ground. Return slowly.",
            "চিত হয়ে শুয়ে হাঁটু ভাঁজ করুন। পেটের পেশী ব্যবহার করে কাঁধ ও মাথা মাটি থেকে সামান্য ওপরে তুলুন।",
            isDurationBased = false,
            caloriesPerMinute = 6f,
            animationId = "crunch"
        ),
        "Abdominal Crunches" to Exercise(
            "Abdominal Crunches",
            "Standard crunch focusing entirely on abdominal contraction to build core strength and burn fat.",
            "পেটের চর্বি কমাতে এবং কোর শক্তি বৃদ্ধি করতে অত্যন্ত কার্যকরী সাধারণ ক্রাঞ্চ ব্যায়াম।",
            isDurationBased = false,
            caloriesPerMinute = 6.2f,
            animationId = "abdominal_crunches"
        ),
        "Knee Push-up" to Exercise(
            "Knee Push-up",
            "A modified push-up done on your knees to make it easier while building arm and chest strength.",
            "হাঁটু মাটিতে রেখে করা পুশ-আপ। এটি নতুনদের শক্তি বাড়ানোর জন্য উপযুক্ত।",
            isDurationBased = false,
            caloriesPerMinute = 5.5f,
            animationId = "knee_push_up"
        ),
        "Butt Bridge" to Exercise(
            "Butt Bridge",
            "Squeeze your glutes and hamstrings to lift your hips toward the ceiling. Hold and repeat.",
            "কোমর ও নিতম্ব শক্ত করে ওপরে তুলুন। এটি নিতম্বের পেশী উন্নত করে।",
            isDurationBased = false,
            caloriesPerMinute = 5f,
            animationId = "butt_bridge"
        ),
        "Side Hop" to Exercise(
            "Side Hop",
            "Jump sideways back and forth to improve agility, coordination, and cardio endurance.",
            "ডানে ও বামে অনবরত লাফ দিন। এটি দ্রুততা ও কার্ডিও স্ট্যামিনা বাড়ায়।",
            isDurationBased = true,
            caloriesPerMinute = 8f,
            animationId = "side_hop"
        ),
        "Flutter Kicks" to Exercise(
            "Flutter Kicks",
            "Lie on your back, lift your feet slightly and flutter them up and down alternately to target lower abs.",
            "চিত হয়ে শুয়ে পা সামান্য উপরে তুলে পর্যায়ক্রমে নিচে-উপরে নাচাতে থাকুন। নিচের পেটের মেদ কমাতে দারুণ কার্যকর।",
            isDurationBased = true,
            caloriesPerMinute = 7f,
            animationId = "flutter_kicks"
        ),
        "V-Up" to Exercise(
            "V-Up",
            "Lie flat, then lift your torso and legs together to form a V-shape, reaching for your toes.",
            "চিত হয়ে শুয়ে এক সাথে পা এবং বডি ওপরে তুলে 'V' অক্ষরের আকার তৈরি করুন ও হাত দিয়ে আঙুল স্পর্শ করুন।",
            isDurationBased = false,
            caloriesPerMinute = 8.5f,
            animationId = "v_up"
        ),
        "Chest Stretch" to Exercise(
            "Chest Stretch",
            "Stand straight, clasp hands behind your back, and lift chest while drawing shoulders back.",
            "সোজা হয়ে দাঁড়িয়ে হাত পিঠের পেছনে নিয়ে আঁকড়ে ধরুন এবং কাঁধ পেছনের দিকে টেনে বুক প্রসারিত করুন।",
            isDurationBased = true,
            caloriesPerMinute = 3f,
            animationId = "chest_stretch"
        )
    )

    // Helper functions to fetch exercise safely
    fun getExercise(name: String): Exercise {
        return exerciseLibrary[name] ?: Exercise(name, "Perform $name exercise.", "$name ব্যায়ামটি করুন।", false, 5f, "default")
    }

    // Build the 30-Day Workout Plan programmatically based on the schedule provided
    fun get30DayPlan(): List<WorkoutDay> {
        val days = mutableListOf<WorkoutDay>()

        for (day in 1..30) {
            val focus = when (day) {
                1, 6 -> Pair("Full Body (Easy)", "পুরো শরীর (সহজ)")
                2, 9 -> Pair("Abs & Core", "পেট ও কোর")
                3, 10 -> Pair("Chest & Arms", "বুক ও বাহু")
                4, 11 -> Pair("Legs Workout", "পা ও থাই")
                5, 12 -> Pair("HIIT Fat Burn", "HIIT ফ্যাট বার্ন")
                7, 14, 21 -> Pair("Rest & Stretch", "বিশ্রাম ও স্ট্রেচ")
                8 -> Pair("Full Body (Medium)", "পুরো শরীর (মাঝারি)")
                13, 15 -> Pair("Full Body Classic", "পুরো শরীর")
                16, 23 -> Pair("Abs & Obliques", "পেট ও কোর")
                17, 24 -> Pair("Arms & Chest Power", "বুক ও বাহু শক্তি")
                18, 25 -> Pair("Legs Strength", "পা")
                19, 26 -> Pair("HIIT Extreme", "HIIT মেদ কমানো")
                20, 27 -> Pair("Fat Burn Blast", "ফ্যাট বার্ন ব্লাস্ট")
                22 -> Pair("Full Body (Hard)", "পুরো শরীর (কঠিন)")
                28 -> Pair("Active Recovery", "অ্যাক্টিভ রিকভারি")
                29 -> Pair("Full Body Challenge", "পুরো শরীর চ্যালেঞ্জ")
                30 -> Pair("Final Challenge", "ফাইনাল চ্যালেঞ্জ")
                else -> Pair("Full Body Workout", "পুরো শরীর")
            }

            // Estimate duration scaling from 8-10 mins on Day 1 to 25-30 mins on Day 30
            val duration = when {
                day <= 4 -> 10
                day <= 7 -> 12
                day <= 14 -> 12
                day <= 21 -> 15
                day <= 28 -> 18
                else -> 25
            }

            // Generate an escalating set of exercises
            val exercises = mutableListOf<WorkoutExercise>()
            
            val isRestDay = (day == 7 || day == 14 || day == 21)
            if (isRestDay) {
                exercises.add(WorkoutExercise(getExercise("Chest Stretch"), 30, 0))
                exercises.add(WorkoutExercise(getExercise("Cobra Stretch"), 30, 0))
                exercises.add(WorkoutExercise(getExercise("Child Pose"), 30, 0))
            } else {
                // Add jumping jack as a warm up
                val jJackSecs = 15 + (day / 2) * 3
                exercises.add(WorkoutExercise(getExercise("Jumping Jack"), jJackSecs, 15))

                // Select core exercises based on the focus area
                when (focus.first) {
                    "Abs & Core", "Abs & Obliques" -> {
                        val repsFactor = 8 + (day / 2)
                        exercises.add(WorkoutExercise(getExercise("Bicycle Crunch"), repsFactor, 15))
                        exercises.add(WorkoutExercise(getExercise("Russian Twist"), repsFactor, 15))
                        exercises.add(WorkoutExercise(getExercise("Leg Raise"), repsFactor - 2, 15))
                        exercises.add(WorkoutExercise(getExercise("Crunch"), repsFactor + 2, 15))
                        val plankSecs = 20 + (day * 2) // Day 1: 20s, Day 10: 40s, Day 30: 80s
                        exercises.add(WorkoutExercise(getExercise("Plank"), plankSecs, 15))
                    }
                    "Chest & Arms", "Arms & Chest Power" -> {
                        val repsFactor = 6 + (day / 2)
                        exercises.add(WorkoutExercise(getExercise("Incline Push-up"), repsFactor + 4, 15))
                        exercises.add(WorkoutExercise(getExercise("Push-up"), repsFactor, 15))
                        exercises.add(WorkoutExercise(getExercise("Diamond Push-up"), repsFactor - 2, 15))
                        exercises.add(WorkoutExercise(getExercise("Triceps Dip"), repsFactor + 2, 15))
                        exercises.add(WorkoutExercise(getExercise("Arm Circle"), 20 + day, 15))
                    }
                    "Legs Workout", "Legs Strength" -> {
                        val squatReps = 15 + (day * 5 / 6) // Day 1: 15, Day 10: 23, Day 30: 40
                        exercises.add(WorkoutExercise(getExercise("Squat"), squatReps, 15))
                        val lungesReps = 8 + (day / 3)
                        exercises.add(WorkoutExercise(getExercise("Lunges"), lungesReps * 2, 15))
                        exercises.add(WorkoutExercise(getExercise("Glute Bridge"), 12 + day, 15))
                        exercises.add(WorkoutExercise(getExercise("Wall Sit"), 20 + day, 15))
                    }
                    "HIIT Fat Burn", "HIIT Extreme" -> {
                        val burpeesReps = 4 + (day / 4)
                        exercises.add(WorkoutExercise(getExercise("Burpee"), burpeesReps, 15))
                        val highKneesSecs = 20 + day
                        exercises.add(WorkoutExercise(getExercise("High Knees"), highKneesSecs, 15))
                        val climbersSec = 20 + day
                        exercises.add(WorkoutExercise(getExercise("Mountain Climber"), climbersSec, 15))
                        exercises.add(WorkoutExercise(getExercise("Side Hop"), 20 + day, 15))
                    }
                    else -> { // Full body days and Challenges
                        val pushupsReps = 8 + (day * 3 / 4) // Day 1: 8, Day 10: 15, Day 30: 30
                        val squatReps = 15 + (day * 5 / 6)
                        val plankSecs = 20 + (day * 2)
                        exercises.add(WorkoutExercise(getExercise("Push-up"), pushupsReps, 15))
                        exercises.add(WorkoutExercise(getExercise("Squat"), squatReps, 15))
                        exercises.add(WorkoutExercise(getExercise("High Knees"), 20 + day, 15))
                        exercises.add(WorkoutExercise(getExercise("Bicycle Crunch"), 12 + day / 2, 15))
                        exercises.add(WorkoutExercise(getExercise("Plank"), plankSecs, 15))
                    }
                }

                // Add cooling down stretches
                exercises.add(WorkoutExercise(getExercise("Cobra Stretch"), 20 + day / 2, 10))
                exercises.add(WorkoutExercise(getExercise("Child Pose"), 20 + day / 2, 0))
            }

            days.add(
                WorkoutDay(
                    dayIndex = day,
                    title = "Day $day",
                    focusArea = focus.first,
                    focusAreaBn = focus.second,
                    durationMinutes = duration,
                    exercises = exercises
                )
            )
        }

        return days
    }
}
