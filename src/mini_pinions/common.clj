;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.common
  "Defines some common state and UI functions. This file has to be separate from
  mini-pinions.core to avoid cyclical dependency issues."
  (:require [quil.core :as q]))

;;;;; Constants

(def width 800)
(def height 600)
(def half-width (/ width 2))
(def half-height (/ height 2))

;;;;; Macros

(defmacro dbg [x]
  `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defmacro style [& body]
  `(do (q/push-style) ~@body (q/pop-style)))

;;;;; Draw

(defn fill-color [rgb]
  (apply q/fill rgb))

(defn fill-grey [x]
  (q/fill x))

(defn clear-background [rgb]
  (apply q/background rgb))

(defn draw-line [[x1 y1] [x2 y2]]
  (q/line x1 y1 x2 y2))

(defn draw-rect [[x y] [w h]]
  (q/rect x y w h))

(defn draw-circle [[x y] r]
  (q/ellipse-mode :radius)
  (q/ellipse x y r r))

(defn draw-ellipse [[x1 y1] [x2 y2]]
  (q/ellipse-mode :corners)
  (q/ellipse x1 y1 x2 y2))

(defn draw-text [s [x y]]
  (q/text s x y))

;;;;; Worlds

;;; Mini Pinion's UI is divided into worlds. Each world is a separate UI view
;;; and manages its own state. Only one world is active at a time.

;;; Each world can implement the following four multimethods (the only required
;;; one is draw). Init is called once and returns an initialized world map.
;;; Update is called every frame and returns an updated world based on the
;;; original world passed to it. Input is called whenver the mouse is pressed or
;;; a key is pressed and returns an altered world as necessary. It should be
;;; used to respond to momentary input (which could occur between updates). Draw
;;; is called every frame and renders the world on screen (returns nothing).

(defmulti init :name)
(defmulti update :name)
(defmulti input :name)
(defmulti draw :name)

(defmethod init :default [world] world)
(defmethod update :default [world] world)
(defmethod input :default [world] world)
(defmethod draw :default [world] nil)

;;;;; Setup

(defn init-state
  "Initializes the Quil sketch's state with the initial World. This should only
  be called once, preferably in the setup function."
  [world-name]
  (q/set-state! :world (atom (init {:name world-name}))))

;;;;; Events

;;; These functions are meant to be passed as event hooks to Quil. These two
;;; functions are the only ones that swap the world state atom.

(defn input-world
  "Update the current World based on an input event."
  []
  (swap! (q/state :world) input))

(defn update-and-draw-world
  "Updates the current World and draws."
  []
  (draw (swap! (q/state :world) update)))
