;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.vector
  "Provides essential Euclidean vector operations."
  (:import java.lang.Math))

;;; These functions are elegant, but very, very inefficient. That being said,
;;; "We should forget about small efficiencies, say about 97% of the time:
;;; premature optimization is the root of all evil" (Donald Knuth).

(defn make [x y] [^float x ^float y])

(def zero (make 0 0))

(defn x [v] (nth v 0))
(defn y [v] (nth v 1))

(defn add [u v]
  (map + u v))

(defn sub [u v]
  (map - u v))

(defn scale [k v]
  (map #(* % k) v))

(defn div [k v]
  (map #(/ % k) v))

(defn dot [u v]
  (reduce + (map * u v)))

(defn normSq [v]
  (dot v v))

(defn norm [v]
  (Math/sqrt (normSq v)))

(defn normalize [v]
  (scale (/ (norm v)) v))

(defn negate [v]
  (scale -1 v))
