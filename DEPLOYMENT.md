# how to deploy the scribl backend (final version)

i solved the "h2 driver" issue by making the app completely auto-configuring. you don't even need to set profiles anymore.

## 1. database (neon.tech)
- get your postgres connection string from [neon.tech](https://neon.tech).
- it looks like `postgres://user:pass@host/db`.

## 2. backend (render)
- click **new** -> **web service**.
- connect your repo.

### environment variables in render
you only need to add these two things:
1. `DATABASE_URL`: (paste your neon connection string exactly as it is)
2. `CORS_ALLOWED_ORIGINS`: `https://your-frontend.vercel.app` (or `*`)

**thats it.** the code now automatically:
- detects the `DATABASE_URL`.
- converts it to the format java needs.
- picks the correct postgres driver without you doing anything.

## 3. local test
it still works perfectly on your local machine with java 25:
```bash
mvn spring-boot:run
```
