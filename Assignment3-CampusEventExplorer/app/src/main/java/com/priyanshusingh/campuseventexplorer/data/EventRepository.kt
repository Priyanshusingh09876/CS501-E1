package com.priyanshusingh.campuseventexplorer.data

/**
 * In-memory source of truth for every campus event.
 *
 * The list is created once and never mutated, so it is safe to read from any thread and from
 * any composable. IDs are explicit (not list indices) so that re-ordering, inserting or removing
 * an event in the future can never change the identity of the remaining events.
 */
object EventRepository {

    val events: List<CampusEvent> = listOf(
        CampusEvent(
            id = 1L,
            title = "Mobile Development Meetup",
            category = "Tech",
            location = "CAS 214",
            time = "Tuesday 6:30 PM",
            description = "Monthly meetup for students building Android and iOS apps. This session " +
                "covers Jetpack Compose navigation patterns, followed by lightning talks and " +
                "open project help. Bring your laptop - beginners are welcome.",
        ),
        CampusEvent(
            id = 2L,
            title = "Fall Career Fair",
            category = "Career",
            location = "GSU Metcalf Ballroom",
            time = "Wednesday 11:00 AM - 3:00 PM",
            description = "Meet recruiters from more than 80 employers hiring for internships, " +
                "co-ops and full-time roles. Business attire is recommended, and the Center for " +
                "Career Development will offer on-the-spot resume reviews near the entrance.",
        ),
        CampusEvent(
            id = 3L,
            title = "Intro to Kotlin Coroutines Workshop",
            category = "Workshop",
            location = "Photonics Center 206",
            time = "Thursday 5:00 PM",
            description = "A hands-on workshop that starts with suspend functions and builds up to " +
                "structured concurrency, flows and testing. Laptops with Android Studio installed " +
                "are required; starter code will be shared at the door.",
        ),
        CampusEvent(
            id = 4L,
            title = "Terriers vs. Northeastern Hockey",
            category = "Sports",
            location = "Agganis Arena",
            time = "Friday 7:30 PM",
            description = "Cheer on the Terriers in the first Green Line rivalry game of the season. " +
                "Student tickets are free with a valid Terrier Card - pick them up at the box " +
                "office up to two hours before puck drop.",
        ),
        CampusEvent(
            id = 5L,
            title = "Midnight Breakfast Study Break",
            category = "Social",
            location = "Marciano Commons",
            time = "Sunday 10:00 PM",
            description = "Pancakes, waffles and bottomless coffee served by faculty and staff the " +
                "night before finals week. Board games, a photo booth and a quiet study corner " +
                "will be set up on the second floor.",
        ),
        CampusEvent(
            id = 6L,
            title = "Faculty Research Spotlight: AI in Healthcare",
            category = "Academic",
            location = "CDS 1750",
            time = "Monday 4:00 PM",
            description = "Three faculty members present current research on machine learning for " +
                "clinical decision support, followed by a moderated Q&A. Undergraduates looking " +
                "for research positions are especially encouraged to attend.",
        ),
        CampusEvent(
            id = 7L,
            title = "Yoga on the Esplanade",
            category = "Wellness",
            location = "Charles River Esplanade, near BU Beach",
            time = "Saturday 9:00 AM",
            description = "An all-levels outdoor yoga class led by FitRec instructors. Mats are " +
                "provided on a first-come basis. The class moves to FitRec Studio 3 if it rains, " +
                "so check the Wellness Center story that morning.",
        ),
        CampusEvent(
            id = 8L,
            title = "Community Service Day",
            category = "Volunteering",
            location = "Meet at Marsh Plaza",
            time = "Saturday 8:30 AM",
            description = "Join hundreds of students volunteering with local nonprofits across " +
                "Boston for a morning of park clean-ups, food bank sorting and tutoring. " +
                "Transportation, breakfast and a T-shirt are included.",
        ),
        CampusEvent(
            id = 9L,
            title = "Student Film Festival",
            category = "Arts",
            location = "Tsai Performance Center",
            time = "Friday 6:00 PM",
            description = "A curated screening of short films written, directed and produced by BU " +
                "students, with an awards ceremony and a reception with the filmmakers " +
                "afterwards. Admission is free; seating is first come, first served.",
        ),
        CampusEvent(
            id = 10L,
            title = "Hack the Charles 24-Hour Hackathon",
            category = "Tech",
            location = "Questrom School of Business",
            time = "Saturday 10:00 AM - Sunday 10:00 AM",
            description = "Form a team, pick a sponsor challenge and build something in 24 hours. " +
                "Mentors, meals and hardware kits are provided. Prizes include interviews with " +
                "sponsor companies and cloud credits for continuing your project.",
        ),
        CampusEvent(
            id = 11L,
            title = "Resume & LinkedIn Clinic",
            category = "Career",
            location = "Yawkey Center for Student Services, 6th floor",
            time = "Tuesday 1:00 PM - 4:00 PM",
            description = "Drop in for a 20-minute one-on-one session with a career advisor. Bring " +
                "a printed resume or your laptop. Walk-ins are welcome, but booking a slot " +
                "through Handshake guarantees a time.",
        ),
        CampusEvent(
            id = 12L,
            title = "International Food Festival",
            category = "Social",
            location = "GSU Plaza",
            time = "Thursday 12:00 PM",
            description = "Student cultural organisations share dishes from more than 25 countries, " +
                "with live music and dance performances throughout the afternoon. Tasting " +
                "tickets are $1 each and all proceeds support the organisations.",
        ),
    )

    /** Distinct categories in display order, used for the quick-filter chips on the list screen. */
    val categories: List<String> = events.map { it.category }.distinct()

    /**
     * Looks up an event by its id. Returns `null` (never throws) for unknown ids so the detail
     * destination can show an "Event unavailable" state instead of crashing.
     */
    fun findById(id: Long): CampusEvent? = events.firstOrNull { it.id == id }

    init {
        // Guard the "unique, stable ID" invariant at class-load time so a copy/paste mistake
        // while adding an event fails loudly in debug builds and unit tests, not silently in a
        // LazyColumn ("Key was already used" crash).
        val duplicateIds = events.groupingBy { it.id }.eachCount().filterValues { it > 1 }.keys
        check(duplicateIds.isEmpty()) { "Duplicate event ids: $duplicateIds" }
    }
}
