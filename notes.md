# Notes

## Learning Clojure

I already know a bit about Lisp having read _The Little Schemer_, but Clojure is different. I am reading [A Brief Beginner's Guide to Clojure][1] to get started with it. I am using [Leiningen][2] to manage the project, which seems to be pretty standard in the Clojure community. To access everything that Processing offers, I am using [Quil][3]. Its README is pretty strange and interesting.

[1]: http://www.unexpected-vortices.com/clojure/brief-beginners-guide/index.html
[2]: https://github.com/technomancy/leiningen
[3]: https://github.com/quil/quil

## Cheat sheets

- [Clojure](http://clojure.org/cheatsheet)
- [Quil](https://github.com/quil/quil/raw/master/docs/cheatsheet/cheat-sheet.pdf)

## State and identity

I am reading through this [article][4] to try to figure out how I will handle state in my code. It is very insightful. I will try to use functional programming as much as possible, but for some things this just won't work: the current GUI screen, the current state of the simulation.

I looked at [clojureOrbit][5], a game written in Clojure hosted on GitHub, to see how others do this. I will probably study some other programs too.

[4]: http://clojure.org/state
[5]: https://github.com/unclebob/clojureOrbit
