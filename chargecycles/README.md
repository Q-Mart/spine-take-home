# Charge Cycles
This application implements the "charge cycles" service described in the design document. It currently contains a
partial implementation of the MVP for the service. Running the `Application:main` method from Intellij should allow
you to start the service.

All business logic is currently written in the main `Application` class, with service registration being handled
by the `RegistrationTask` class. You can find an API specification at `api-spec.yaml`. The `Stubs` class contains
stubs for all HTTP calls to API endpoints outside of this service.

Some things we need to address before I consider this MVP ready:
- Re-organise/redesign how we handle error propagation and error handling. The current way of doing is quite clunky
  and primitive. On top of that, the endpoint isn't sticking to the API spec when it handles an error, so we need
  to fix that.
- Write a set of integration/unit tests to properly validate this
- Changing approach to stubbing so that the application code calls more representative (i.e. methods you'd use in
  the real world) methods when calling an API endpoint. The current approach is good at illustrating my intent,
  but probably isn't realistic as it's not based on a specific library.
- Add a Dockerfile and verify that it can run on a k8s cluster

I unfortunately was at the time limit while writing the code for this service, which is why I have either not
tackled this issues, or used quite primitive ideas to get something done (e.g. the `Stubs` class).