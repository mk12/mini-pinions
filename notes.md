# Development Notes

## Learning Clojure

**[16 Oct. 2013]**
I already know a bit about Lisp having read _The Little Schemer_, but Clojure is different. I read [A Brief Beginner's Guide to Clojure][1] to get started with it. I am using [Leiningen][2] to manage the project, which seems to be pretty standard in the Clojure community. To access everything that Processing offers, I am using [Quil][3]. (Its README is interesting and a bit strange, mostly because half of it is poetry. There is also a reference to [_why][4] in it.)

[1]: http://www.unexpected-vortices.com/clojure/brief-beginners-guide/index.html
[2]: https://github.com/technomancy/leiningen
[3]: https://github.com/quil/quil
[4]: http://en.wikipedia.org/wiki/Why_the_lucky_stiff

Here are a couple cheat sheets that may be useful later on:

- [Clojure](http://clojure.org/cheatsheet)
- [Quil](https://github.com/quil/quil/raw/master/docs/cheatsheet/cheat-sheet.pdf)

## Functional programming

**[17 Oct. 2013]**
I read through this [article][5] to learn more about the functional programming way of handling state. It is very insightful. I will try to use functional programming as much as possible, but for some things this just won't work: the current GUI screen, the current state of the simulation. I will need to use "identities" for these.

**[18 Oct. 2013]**
I watched this [video lecture][6] to learn more about the philosophy behind Clojure and functional programming in general. I was linked to it from this Stack Overflow [question][7]. The lecturer, Rich Hickey, is actually the author of Clojure. If you don't know anything about functional programming, I would highly recommend watching the lecture. It goes into much more depth than the article I mentioned above. I know I am spending a lot of time on this kind of thing, but I think it is worth learning. 

Languages with manual memory management have significant _incidental complexity_. Garbage-collected OOP languages such as Java are better, but they still have incidental complexity of another kind: time management. The building block with the least incidental complexity is a pure function: all it does is convert input to output; time is irrelevant. Mutable objects pretend to have a notion of time, but it isn't concrete. The problem is inherent in the philosophy of OOP, and it becomes much more apparent when you try to write multithreaded programs. Objects are useful for modelling the world around us, but _changing_ objects are just a construct of our minds. A falling ball is _not_ a ball that changes. It is a sequence of immutable ball values that are related. Each future version of the ball is function of the past version. Some definitions (taken from one of the slides):

- **value**: an immutable magnitude, quantity, number ... or immutable composite thereof
- **identity**: a putative entity we associate with a series of causally related values (states) over time
- **state**: the value of an identity at a moment in time
- **time**: relative before/after ordering of causal values

[5]: http://clojure.org/state
[6]: http://www.infoq.com/presentations/Are-We-There-Yet-Rich-Hickey
[7]: http://stackoverflow.com/questions/9132346/clojure-differences-between-ref-var-agent-atom-with-examples

## Modelling the user interface

**[19 Oct. 2013]**
I was having a hard time getting started with this, so I asked [a question][8] on Stack Overflow. One person answered, and they linked me to this [blog post][9], which proved to be pretty useful. I am going to try to model everything as simply and elegantly as possible and worry about performance later (if I need to). I think that once I get past this hurdle of modelling state (the UI state and the game simulation state), things will go much quicker.

I found _another_ [video lecture][10] by Rich Hickey. He goes into more detail about persistent data structures and the specific time management constructs found in Clojure.

Huzzah! I have finally got a system working where the state of the UI and of the game world is properly managed. I can easily switch between UI screens, and the game simulation can be advanced by making the next state a function of the previous state. I was a bit dismayed when I noticed that the frame rate was struggling between 30 and 40 FPS, but I tried changing the rendered to OpenGL and it immediately jumped to 60 FPS, indicating that the bottleneck was the renderer, not any inherent slowness in Clojure.

[8]: http://stackoverflow.com/questions/19461857/modelling-game-ui-screens-in-clojure
[9]: http://stevelosh.com/blog/2012/07/caves-of-clojure-02/
[10]: http://www.infoq.com/presentations/Value-Identity-State-Rich-Hickey

## Taking flight

**[20 Oct. 2013]**
The main menu now has some nifty buttons. But it's time to focus on the main game. I don't know when the halfway checkpoint deadline is, but it probably isn't too far away. A thought occurred to me as I imagined the hills and valleys of Fledge's world (the bird is officially called Fledge, by the way): it looks an awful lot like a sinusoidal curve. Forget Bézier and other similarly complicated stuff – I will just use trig functions to create the landscape!

I found [a blog dedicated to Tiny Wings physics][11] – imagine that! After actually reading it, though, I realized that it isn't very useful to me.

**[21 Oct. 2013]**
It may not be especially fast, but I am making progress. I love Clojure, but functional programming tends to twist your brain into a pretzel when you are (like me) fairly new to it. That isn't to say that I am much less productive with it than I would be with Java or some other OOP language at the moment – the stuff I am modelling right now (the curves that define a level) really does take quite a bit of thinking. I think I have implemented it in the best way possible. The _divide and conquer_ method is really helping me.

[11]: http://tinywingsphysics.blogspot.ca

## Still taking flight

**[22 October 2013]**
The halfway checkpoint deadline, 24 October 2013, is looming on the horizon like a big thing that looms, so I am trying to get things done quickly. I now have a nice set of button functions that make it super easy to use buttons in the mani menu, and I will also use them in other Worlds. But now I am entirely focused on the Game world – I need to get Fledge moving!

It is 1:15 a.m., and Fledge is moving! He is racing along horizontally, allowing the sine landforms to be traced in all their glory. All that is left for the halfway checkpoint is the physics.