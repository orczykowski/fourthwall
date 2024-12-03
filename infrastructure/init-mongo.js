db.createUser({
    user: "movie_usr", pwd: "test123", roles: [{
        role: "readWrite", db: "movie_db"
    }]
});

db.createCollection("movies");
db.movies.insertMany([{
    "imdbId": "tt0232500",
    "title": "The Fast and the Furious",
    "description": "Los Angeles police officer Brian O'Conner must decide where his loyalty really lies when he becomes enamored with the street racing world he has been sent undercover to end it.",
    "durationInSeconds": 6390,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama",
}, {
    "imdbId": "tt0322259",
    "title": "Former cop Brian O'Conner is called upon to bust a dangerous criminal and he recruits the help of a former childhood friend and street racer who has a chance to redeem himself.",
    "description": "",
    "durationInSeconds": 6450,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt0463985",
    "title": "The Fast and the Furious: Tokyo Drift",
    "description": "A teenager becomes a major competitor in the world of drift racing after moving in with his father in Tokyo to avoid a jail sentence in America.",
    "durationInSeconds": 6240,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt1013752",
    "title": "Fast & Furious",
    "description": "Brian O'Conner, back working for the FBI in Los Angeles, teams up with Dominic Toretto to bring down a heroin importer by infiltrating his operation.",
    "durationInSeconds": 6420,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt1596343",
    "title": "Fast Five",
    "description": "Dominic Toretto and his crew of street racers plan a massive heist to buy their freedom while in the sights of a powerful Brazilian drug lord and a dangerous federal agent.",
    "durationInSeconds": 7800,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt1905041",
    "title": "Fast & Furious 6",
    "description": "Hobbs has Dominic and Brian reassemble their crew to take down a team of mercenaries, but Dominic unexpectedly gets sidetracked with facing his presumed deceased girlfriend, Letty.",
    "durationInSeconds": 7800,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt2820852",
    "title": "Furious 7",
    "description": "Deckard Shaw seeks revenge against Dominic Toretto and his family for his comatose brother.",
    "durationInSeconds": 8220,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt4630562",
    "title": "The Fate of the Furious",
    "description": "When a mysterious woman seduces Dominic Toretto into the world of terrorism and a betrayal of those closest to him, the crew face trials that will test them as never before.",
    "durationInSeconds": 8160,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
}, {
    "imdbId": "tt5433138",
    "title": "F9: The Fast Saga",
    "description": "Dom and the crew must take on an international terrorist who turns out to be Dom and Mia's estranged brother.",
    "durationInSeconds": 8580,
    "ratingImDb": "0.0",
    "userRating": 0.0,
    "category": "drama"
},]);