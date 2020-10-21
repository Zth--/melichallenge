# Hi!

I made this API using Java 11 and Spring as the framework.
This is my first time implementing a scalable API and my first time using Google App Engine.
I tried to make this document as complete as possible, so here it goes.
The whole explanation is after the endpoints so please don't skip it.

# The endpoints

## Check if certain DNA is human or mutant

### Request
`POST /mutant`
```
curl -i -d '{"dna":["AAAACA","CTCTCT","TCTCTC","GGGTTT","TAAAGG","GGGTTT"]}' http://localhost:8080/mutant -H "Content-Type: application/json"
```

### Response
```
HTTP/1.1 200 
Content-Length: 0
Date: Wed, 21 Oct 2020 18:10:09 GMT
```

It will answer 200 if it's mutant. 403 if it's a human. 400 if it's a bad request.

## Checking the stats

### Request

`GET /stats`
```
curl -i http://localhost:8080/stats
```

### Response

```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 21 Oct 2020 18:13:38 GMT

{"count_human_dna":1,"count_mutant_dna":2,"ratio":0.5}
```

## Health check

### Request

`GET /isAlive`
```
curl -i http://localhost:8080/isAlive/
```

### Response

```
HTTP/1.1 200 
Content-Length: 0
Date: Wed, 21 Oct 2020 18:04:30 GMT
```

###


# Level 1

The algorithm can be found on the file DnaSolver and it is pretty much self-explanatory.

It is as efficient as possible.

It receives a (NxN) matrix and returns true if there are four consecutive letters (possible letters are A,T,G,C).

Pretty much like the game Connect 4 but with four colours instead of only two.

In the worst case scenario it has to check all the possible combinations, which of course gets more expensive with larger matrices.

It doesn't calculate "impossible paths", but that optimization only matters for relatively small matrices.


# Level 2

There's not much to be said about this level, it is a simple "/mutant" endpoint that works with the previous algorithm.

It has to answer 200-OK if it's a mutant and 403-Forbidden if it's a human.

What I had to consider here is what to do if Magneto sends invalid dna sequences, should I answer 400-Bad Request or

stick to 403-Forbidden? I talk a little more about this on the file DnaSolver.

# Level 3

This level is far more complex than the previous two, and I spent most of my time tackling this one.

First I'll talk a little bit about the design that I chose for the problem given the considerations

## Constraints

There's a few things that I had to consider before diving deep into the challenge, in order of importance:

 * How quickly does the database need to reflect new information? Does it have to be real time?
 * RPS (requests per second) fluctuate a lot (up to a million), but I have no information on how these requests are
   composed. Are there repeated requests? If so, is it a considerable amount?
   Should I worry about it? What about the dna sequence?
   Will the matrices be always (6x6) as shown on the examples or will Magneto send me different sizes?
 * How accurate should the `/stats` response be? Should I count previously processed dna sequences?
 
## Design

Based on the aforementioned constraints I had to choose a few things. 
 * Do I need a cache? Given that the algorithm is super cheap for small matrices there'd be no point in implementing one.
 Magneto should be sending a lot of repeated large requests to justify adding a cache. So the answer is no, I don't need
 one.
 * Do I need real time DB? The challenge doesn't specify it, so I play safe here and go with a no. Implementing one
 would be _hard_ (at least for me, I've never done it!)

Even without a real time DB I needed some kind of thread-safe global datastore to keep a queue of data to-be-inserted.

I could've used Redis or some other thing but honestly that would've been an overkill.

So I googled a bit for a data structure and found that ConcurrentLinkedQueue covered my needs.

Well, at this point I had to decide what strategy to use to push that data to the DB. I came up with a simple background

job that checks the queue every second and tries to batch insert it (there's only one background job running at a time)

Cool, and what about the DB itself? Well, to be honest this was my main concern. I ran into multiple problems while
trying to tackle the "only one row per dna" problem. I thought and tried different things but I ended up keeping it
simple.

Amongst the things I considered:
 * My first idea was to do a batch insert of the queue while ignoring duplicates. 
 * Another idea was to dump the queue into a temporary table and then merge to a main table without the duplicates.
 * The last idea was to dump the queue into a file and then insert it into the DB. I read that this performs really well.

To be completely honest I couldn't get the batch insert to properly work in a reasonable amount of time so I decided to
ship it without it. At this point I think I've addressed all the levels. Spending extra time into researching
and fixing this inconvenience properly wouldn't have been a good trade-off of my time.

So I decided to go with 1 server - 1 db architecture. 

## Stats

When is the best moment to increment the stats counter? After processing? Before sending the batch to the DB?
After doing so? Another background job that counts the DB records every N minutes?

Well, after processing and before inserting into the buffer is not a good idea because I might be counting duplicates.
Before or after sending the batch to the DB might be a good time but it only makes sense if there's only one server (the approach I went with).
If there are multiple APIs working horizontally then the counter wouldn't be accurate and different APIs would 
answer different stats depending on how the load balancer works.
Exposing an endpoint and expecting someone else to update our info is not too bad of an idea but it can be better.
(Or maybe do the opposite, a single API that acts as a service to the many horizontal APIs).

At this point I stopped thinking about it.
Since I'm making this a single server with a single DB I decided to go with counting before sending a batch. The background
job does it. It can be better but I didn't want to overcomplicate it and it's super easy to modify.

Ah, I almost forgot to mention that the StatsService counts the number of mutants and humans on startup (from the DB, of course).

There's also a few extra things I had to considerate when designing the "ratio" value.
I decided not to include it here because this document is getting large enough, but you can ask me.

## Docker

On a real-world scenario I would've gone with a docker-compose file.

But I think it would've been out of the scope of the challenge and I had a lot of things to do already.

## The bottleneck

While not perfect I consider my design to be a good MVP (not for a real world scenario but for a challenge).

It won't be able to hold a million RPS for a long period of time because the "cleaning" of the memory is slow (only a few thousand
rows per second on my testings). I think I can come up with different ideas on how to approach this problem but I won't talk
about them here.

## The final test

Again, on a real world scenario a final test is mandatory. After skimming through some documents I realized that
creating a million RPS is a daunting task and I decided not to do it. Please correct me if there's an easy way.

## Last bits

Pulling this off wasn't the easiest thing in the world, so I really hope that this is good enough.
I'd consider databases my weakest point, I don't work with them on my current job so I don't get the opportunity
to experiment with them. I'm eager to learn, though. Working with high availability services and scalable systems
would be very fun.

Cheers,

Adrian