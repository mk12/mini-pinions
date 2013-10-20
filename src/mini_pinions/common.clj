(ns mini-pinions.common
  (:require [quil.core :as q]))

;;; There are two state objects in Mini Pinions. The first, view, is the state
;;; associated with a particular UI screen. It includes the name of the screen
;;; and other resources, for example images. The second, world, is the state
;;; that gets updated every frame. This includes the simulation values for the
;;; game as well and it can also be used for animations.

(defn init-state []
  (q/set-state!
    :view (atom nil)
    :world (atom nil)))

(def view #(q/state :view))
(def world #(q/state :world))

(defmulti init-view :name)
(defmulti init-world :name)
(defmulti draw :name)
(defmulti mouse-pressed :name)
(defmulti key-pressed :name)

(def draw-current-view #(draw @(view)))
(def mouse-current-view #(mouse-pressed @(view)))
(def key-current-view #(key-pressed @(view)))

(defn show-view [view-name]
  (reset! (view) (init-view {:name view-name}))
  (reset! (world) (init-world @(view))))
