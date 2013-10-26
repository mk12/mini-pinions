;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.vector
  "Provides essential Euclidean vector operations."
  (:import java.lang.Math))

;;; These functions are elegant, but very, very inefficient. That being said,
;;; "We should forget about small efficiencies, say about 97% of the time:
;;; premature optimization is the root of all evil" (Donald Knuth).

;;; UPDATE: I have made it slightly more efficient in case the vectors are
;;; slowing things down. It seems that graphics is the bottleneck, but I am
;;; going to leave it implemented like this.

(defn make [x y] [^float x ^float y])

(def zero (make 0 0))

(defn x [v] (get v 0))
(defn y [v] (get v 1))

(defn add [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn sub [[x1 y1] [x2 y2]]
  [(- x1 x2) (- y1 y2)])

(defn scale [k [x y]]
  [(* x k) (* y k)])

(defn div [k [x y]]
  [(/ x k) (/ y k)])

(defn dot [[x1 y1] [x2 y2]]
  (+ (* x1 x2) (* y1 y2)))

(defn norm-sq [v]
  (dot v v))

(defn norm [v]
  (Math/sqrt (norm-sq v)))

(defn normalize [v]
  (div (norm v) v))

(defn negate [v]
  (scale -1 v))
