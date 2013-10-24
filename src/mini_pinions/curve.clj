;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.curve
  "Defines functions for working with curves, the building blocks of levels."
  (:require [quil.core :as q]
            [mini-pinions.vector :as v])
  (:import java.lang.Math))

;;;;; Curve

(defn make-curve
  "Makes a curve beginning at the start vector, with the given amplitude,
  initially going up or down according to direction, and completing half-cycles
  (from min to max or vice versa) within the given width."
  [start height direction half-cycles width]
  (let [sign (direction {:up 1 :down -1})]
    {:q (+ (v/y start) (* sign 0.5 height))
     :a (* -1 sign 0.5 height)
     :k (/ (* half-cycles Math/PI) width)
     :p (v/x start)
     :w width}))

(defn curve-y
  "Calculates the y-value of the curve at a particular x-value."
  [curve x]
  (let [x-p (- x (:p curve))]
    (if (<= x-p (:w curve))
      (-> x-p
          (* (:k curve))
          Math/cos
          (* (:a curve))
          (+ (:q curve))))))

(defn curve-m
  "Calculates the instantaneous slope of the curve at a particular x-value. The
  formula was obtained by using Wolfram Alpha to find the derivative of the
  transformed cosine function."
  [curve x]
  (let [x-p (- x (:p curve))]
    (if (<= x-p (:w curve))
      (-> x-p
          (* (:k curve))
          Math/sin
          (* (:a curve) (:k curve) -1)))))

;;;;; Path

;;; A "curve definition" refers to the keys
;;;     :height, :direction, :half-cycles, :width.
;;; A "curve" refers to the keys
;;;     :q, :a, :k, :p, :w.
;;; A "path" refers to a sequence of curves.

(defn curve-end
  "Calculates the end-point of a curve definition given its start-point."
  [start curve-def]
  (let [sign ((:direction curve-def) {:up 1 :down -1})
        odd (mod (:half-cycles curve-def) 2)
        end-y (* sign odd (:height curve-def))]
    (v/add start
           (v/make (:width curve-def) end-y))))

(defn make-path
  "Makes a sequence of curves by joining the curve definitions end to end. The
  position of the first curve must be provided."
  [start curve-defs]
  (map #(make-curve %1
                    (:height %2)
                    (:direction %2)
                    (:half-cycles %2)
                    (:width %2))
        (reductions curve-end start curve-defs)
        curve-defs))

(defn path-val
  "Calculate the y-value or m-value of the path at a particular x-value."
  [path x y-or-m]
  (if-let [curve (last (take-while #(<= (:p %) x) path))]
    ((y-or-m {:y curve-y :m curve-m}) curve x)))

(def path-y (memoize path-val))

;;;;; Draw

(defn draw-path
  "Draws a section of a path given by [start,end] with resolution vertices."
  [path [start end] resolution]
  (q/begin-shape)
  (q/vertex start 0)
  (doseq [x (concat (cons start (range start (+ end resolution) resolution))
                    [end])]
    (if-let [y (path-y path x :y)]
      (q/vertex x y)))
  (q/vertex end 0)
  (q/end-shape :close))
