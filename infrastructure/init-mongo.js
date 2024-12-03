db.createUser({
    user: "movie_usr",
    pwd: "test123",
    roles: [
        {
            role: "readWrite",
            db: "movie_db"
        }
    ]
});