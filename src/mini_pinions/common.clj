;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.common
  "Defines some common state and UI functions. This file has to be separate from
  mini-pinions.core to avoid cyclical dependency issues."
  (:require [quil.core :as q]))

;;;;; Constants

(def width 800)
(def height 600)
(def center-x (/ width 2))
(def center-y (/ height 2))

;;;;; Macros

(defmacro dbg [x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

;;;;; Convenience functions

(defn draw-rect [[x y] [w h]]
  (q/rect x y w h))

(defn draw-text [s [x y]]
  (q/text s x y))

;;;;; Worlds

;;; Mini Pinions UI is divided into Worlds. Each World is a separate UI view and
;;; manages its own state. Only one World is active at a time.

(declare make-world)
(defn init-state
  "Initializes the Quil sketch's state with the initial World. This should only
  be called once, preferably in the setup function."
  [world-name]
  (q/set-state! :world (atom (make-world world-name))))

;;; Each World can implement the following four multimethods (the only required
;;; one is draw). Init is called once and returns an initialized World map.
;;; Update is called every frame and returns an updated World based on the
;;; original World passed to it. Input is called whenver the mouse is pressed or
;;; a key is pressed and returns an altered World as necessary. It should be
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

(defn make-world
  "Creates and initializes a new World given its name."
  [world-name]
  (init {:name world-name}))

(defn input-world
  "Update the current World based on an input event."
  []
  (swap! (q/state :world) input))

(defn draw-world
  "Updates the current World and draws."
  []
  (draw (swap! (q/state :world) update)))
