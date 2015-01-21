# make-change

An implementation of [Making
Change](http://craftsmanship.sv.cmu.edu/katas/making-change) kata. Focus
is on testing using Clojure's
[test.check](https://github.com/clojure/test.check) - a property-based testing
framework.

## Rationale

I have never used a property-based testing framework before; though, I recall
constructing (or maintaining) unit tests performing property-based testing in a
more ad hoc manner.

At this point, I am also not yet entirely convinced on the efficacy vs.
(developer) efficiency ("bang for the buck") of property-based testing,
especially in lieu of classic unit testing (i.e., manually encode
input/output expectations) for simple functions. However, I do have a feeling
that property-based testing can pay-off in systems integrating numerous
"moving" components.

As such, the primary focus for writing this code was to learn and use Clojure's
test.check library. With that said, I do attempt to provide a solution to the
problem of writing "generalized" test specs raised in the article [Check Your
Work](http://blog.8thlight.com/connor-mendenhall/2013/10/31/check-your-work.html)
by [@ecmendenhall](https://github.com/ecmendenhall).

> In preparing this post, I started with the Coin Changer Kata, assuming that
 its tricky edge cases would be a good match for generative tests. But after
 writing a few Simple-check tests, I was stuck. I knew the specific case I was
 trying to describe—changing 80 cents with denominations of 25, 20, and 1 should
 return four 20 cent coins instead of three quarters and five pennies—but I was
 left scratching my head when I tried to generalize it.

## Synopsis

The test specs  `smallest-change` and `double-amount-change`in [core_test.clj]
(test/making_change/core_test.clj) provide a basis for generalized
property-based testing for the "Making Change" kata. With validation performed
over hundreds of auto-generated test cases, the test specs will catch an
improper solution. For instance, the incorrect function `greedy-change` in
[core.clj](src/making_change/core.clj) fails the `double-amount-change` test
fairly quickly.

## Thoughts

As a complete newbie to the field of property-based testing, the following
observations do not carry much weight -- except perhaps with other newbies. My
thoughts will definitely change as I gather more experience with property-based
testing. Nevertheless, here goes (and thanks for indulging me by reading on):

It seems the principle to effectively use property-based test frameworks for
problems that require recursive application is to also think of the test spec
recursively. Firstly, write a test spec using the simplest base case (see
`smallest-change` spec). Next, build a more "sophisticated" spec that
exploits the base case along with an additional properly (see
`double-amount-change` spec). Since the framework will generate
hundreds of test cases (or even more - you control the knob), any violations
will be caught without having to even think of a counter example yourself.

## License

Copyright © 2015 Jay A. Patel

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
