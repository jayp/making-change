# make-change

An implementation of [Making
Change](http://craftsmanship.sv.cmu.edu/katas/making-change) kata. Focus
is on testing using Clojure's
[test.check](https://github.com/clojure/test.check) - a property-based testing
framework.

## Notes

While the primary focus for writing this code was to learn and use Clojure's
test.check library, I attempt to provide a solution to the problem
raised in the article [Check Your
Work](http://blog.8thlight.com/connor-mendenhall/2013/10/31/check-your-work.html)
by [@ecmendenhall](https://github.com/ecmendenhall).

> In preparing this post, I started with the Coin Changer Kata, assuming that
 its tricky edge cases would be a good match for generative tests. But after
 writing a few Simple-check tests, I was stuck. I knew the specific case I was
 trying to describe—changing 80 cents with denominations of 25, 20, and 1 should
 return four 20 cent coins instead of three quarters and five pennies—but I was
 left scratching my head when I tried to generalize it.

The test specs  `smallest-change` and `double-amount-change`in [core_test.clj]
(test/making_change/core_test.clj) provide a basis for the correct solution.
With testing done over hundreds of auto-generated test cases, the test specs
will catch an improper solution. For instance, the incorrect function
`greedy-change` in [core.clj](src/making_change/core.clj) fails the
`double-amount-change` test fairly quickly.

As a newbie to the field of property-based testing, the following observation
is probably not worth much. My thoughts and opinions will likely change with
more experience. Nevertheless, here goes: it seems the principle to effectively
use property-based test frameworks is to write test specs built on recursive
thought. Firstly, write a test spec using the simplest base case (see
`smallest-change` spec). Next, build a more complicated spec that exploits
the base case along with an additional properly (see `double-amount-change`
spec). Since the framework will generate hundreds of test cases (or even more -
you control the knob), any violations will be caught without having to
even think of a counter example yourself.

## License

Copyright © 2015 Jay A. Patel

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
