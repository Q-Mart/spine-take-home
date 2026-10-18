I'm not sure we need the `error` or `code` fields in the `ResponseError` types. The error and code seem to be the
same thing (textual representation of a HTTP status), which is what we already get in the HTTP response.

I think for errors, we just need to return a string with a reason. In that case, we can just return this as text,
rather than a JSON object