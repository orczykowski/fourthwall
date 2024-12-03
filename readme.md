At the very beginning, I would like to apologize for the delay.

What Has Been Accomplished:

- I decided to write the application using a hexagonal architecture. Initially, it was supposed to have three layers,
  but the logic started to expand. Moreover, there is theoretically room for functional growth in the future, so this
  approach seemed reasonable to me.
- Framework: Spring.
- Database: I chose MongoDB for data management. (
  I placed a Docker Compose file in the /infrastructure directory to allow the application and its dependencies to be
  run locally.)
- HTTP Client: Currently, the HTTP client uses only the simplest retry policy (I didn’t have time to implement
  Resilience4j). It might have been just as quick to use it, but the client still needs additional configuration (e.g.,
  connection pooling, etc.). At the moment, everything is set to defaults.
- Domain Layer: I placed all the logic and business objects in the domain package. Entities from the domain layer do
  not "leak" outside to maintain encapsulation of the logic and avoid potential problems.
- The main object is Movie entity, which contains a copy of the information along with a summary of ratings.
- The ratings are handled independently, and updates to the Movie object are performed using business events (
  ApplicationEvent).
- Imdb ratings are updated every 2 hours (defined in property file) in scheduler
- I made an effort to model the graphics/pricing for movie screenings as accurately as possible. They include very basic
  validations; however, some elements are still missing to better reflect a more realistic scenario.
- Access to endpoints is managed using Spring Security, and the user database is currently loaded into memory directly
  from a properties file
- api docs are available on docs/v3/api-docs

<p style="color: red">
Unfortunately, the thing that is lacking the most are the tests. I barely managed to write any at all. Unfortunately, I
attempted to do it in Kotlin (it was actually my first time writing tests in Kotlin), and I failed when it came to
selecting the right libraries. Unfortunately, all the standard techniques, like MOCK BEAN for Mvc tests or similar,
didn’t work. Time is running out, so I decided to submit what’s currently in the repository. :
</p>