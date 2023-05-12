## Instructions for the assignment
1. Clone this repository on your machine.
1. Use your IDE of choice to complete the assignment.
1. When you are done with the solution and have pushed it to the repo, [you can submit the assignment here](https://app.snapcode.review/submission_links/1af6f4c5-c476-4c92-b7a6-1aaf49af44c3).
1. Once you submitted your solution, your access to the repository will be revoked. So make sure that before you do submit the solution, that you have completed the problem and pushed all your changes.

## Before you start
### Why complete this task?

We want to make the interview process as simple and stress-free as possible. That’s why we ask you to complete the first stage of the process from the comfort of your own home.

Your submission will help us to learn about your skills and approach. If we think you’re a good fit for our network, we’ll use your submission in the next interview stages too.

### About the task

We’ll be using a simple shopping cart, similar to those used by e-commerce websites, as the domain for this problem.

There’s no time limit for this task. We find most people complete the exercise in around 90 minutes.

### Tips on what we’re looking for

1. **Test coverage**

Your solution should:

- be developed ‘test-first’
- have good unit tests
- cover common paths
- be self-contained and not rely on external systems when running tests
- integration tests should run against stubs using something like [WireMock](https://wiremock.org/), [MockServer](https://www.mock-server.com/) or similar, not the real API

2. **Simplicity**

We value simplicity as an architectural virtue and a development practice. Solutions should reflect the difficulty of the assigned task, and shouldn’t be overly complex. We prefer simple, well tested solutions over clever solutions.

Please avoid:

- layers of abstraction
- patterns
- custom test frameworks
- architectural features that aren’t called for
- a web, desktop, command line or any other kind of app

3. **Self-explanatory code**

The solution you produce must speak for itself. Multiple paragraphs explaining the solution is a sign that the code isn’t straightforward enough to understand on its own.

4. **Dealing with ambiguity**

If there’s any ambiguity, please add this in a section at the bottom of the README. You should also make a choice to resolve the ambiguity.

## Begin the task

Create a shopping cart package that facilitates 2 basic capabilities.

1. Add multiple products to the cart. A product has a name and price. You should be able to specify how many of each product is being added to the cart, and provide a means to observe the resulting state.

2. Calculate the totals:
   1. Cart subtotal (sum of price for all items)
   2. Tax payable (charged at 12.5% on the subtotal)
   3. Total payable (subtotal + tax)
   
Pricing data for each product should be retrieved via an HTTP call. You can find example pricing data for a set of sample products at the URL’s below. Prices should be rounded up where required.

You should assume that the product name (lowercase) matches the file name. Use whatever libraries you like to fetch and parse the JSON.

## Valid Product Information URLs
- https://equalexperts.github.io/backend-take-home-test-data/cheerios.json
- https://equalexperts.github.io/backend-take-home-test-data/cornflakes.json
- https://equalexperts.github.io/backend-take-home-test-data/frosties.json
- https://equalexperts.github.io/backend-take-home-test-data/shreddies.json
- https://equalexperts.github.io/backend-take-home-test-data/weetabix.json

## Sample based on the data
The below is a sample with the correct values you can use to confirm your calculations
```
  Add 1 × cornflakes @2.52 each
  Add another 1 x cornflakes @2.52 each
  Add 1 × weetabix @9.98 each
  
  Then: 
  
  Cart contains 2 x conflakes
  Cart contains 1 x weetabix
  Subtotal = 15.02
  Tax = 1.88
  Total = 16.90
```
