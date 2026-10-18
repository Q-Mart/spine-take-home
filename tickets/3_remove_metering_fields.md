The `interval` field in the metering 200 response is a bit ambiguous. There is no example value, or description. I
I assume this to be the interval between each data point. As it is stated elsewhere that all values have a 1 second
refresh rate, we can always assume the interval is one second. Thus, we can remove this field. There are probably
some efficiency gains by upping the interval and having less data points. I'm currently taking the notion that we
want our metering data to be as granular as possible when ingesting, so think we should remove the field.

Additionally, we don't need the `from`, `to`, or `assetId` fields, as we already have them as part of the request
parameters. Therefore, we should remove these fields.

This would just leave us with the `data`. I don't think we need to enclose this list of data within a JSON field, so
instead, let's just return all the data as a top-level JSON array.
