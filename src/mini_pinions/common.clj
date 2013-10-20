;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.common
  "Defines some common state and UI functions. This has to be separate from
  mini-pinions.core to avoid cyclical dependency issues."
  (:require [quil.core :as q]))

;;; Mini Pinions UI is divided into Worlds. Each World is a separate UI view and
;;; manages its own state. Only one World is active at a time.

(defn init-state [] (q/set-state! :world (atom nil)))
(defn world [] (q/state :world))

(defmulti init :name)
(defmulti update :name)
(defmulti draw :name)

(defn show-world [world-name]
  (reset! (world) (init {:name world-name})))

(defn draw-world []
  (draw (swap! (world) update)))
