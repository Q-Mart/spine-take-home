The documentation for the `from` and `to` fields in the Metering endpoint do not specify whether the endpoint
collects metering points inclusive of these specified time ranges.

We should adopt a project-wide standard of some sort. I've chosen that all `to` and `from` parameters are inclusive
of the specified values