(ns csspool.core
  (:require [clojure.walk :as walk :only [postwalk]]
            [csspool.cross :as cross :refer [default-browsers]]))

(def browser-pres [nil "-webkit-" "-moz-" "-o-"])

(def minimal {:lb \{ :rb \} :sp nil :nl nil})
(def pretty {:lb "{\n" :rb "}\n" :sp \space :nl \newline})
(def ^:dynamic *format* minimal)
(defmacro with-pretty [& body] `(binding [*format* pretty] ~@body))

(defmacro with-format
  "ANAPHORIC! Captures `lb rb sp` and `nl` (from `*format*`), representing
  braces, space and newline."
  [& body]
  `(let [{:keys ~'[lb rb sp nl]} *format*] ~@body))

(defn in-pairs
  "Break sequence or map into lazy seq of kv pairs removing layers of sequence
  as necessary. Throws for odd number of elements (valueless keys)."
  [xs]
  (when-let [xs (seq xs)] ; the let permits use with maps
    (if (sequential? (first xs))
      (lazy-cat (in-pairs (first xs)) (in-pairs (next xs)))
      (lazy-seq (cons [(first xs) (second xs)]
                      (in-pairs (nnext xs)))))))

(defn prepare
  "Make tree ready for processing by producing seq (something) like
  `([selector ([prop val])] [sel1 ([p1 v1] [p2 v2])])`."
  [xs]
  (map (fn [[k v]] [k (in-pairs v)]) (in-pairs xs)))

(defn render-rule
  "Output css string for each selector/ruleset pair."
  ([[sel rules]]
   (with-format
     (let [g (fn [[k v]]
               (str sp sp (name k) \: sp (if (number? v) v (name v)) \; nl))]
       (str (name sel) lb (reduce str (map g rules)) rb))))
  ([sel rules] (render-rule [sel rules])))

(defn render
  "Output css for map or seq of selector/ruleset pairs"
  [c]
  (reduce str (map render-rule c)))

(def quick-render
  "Prepare and render css in single step, without any processing."
  (comp render prepare))

(defn prefix
  "Produce seq of `k v` pairs where each `k` is prefixed with one of `ps`."
  ([ps k v]
   (map (fn [x] [(str x (name k)) v]) ps))
  ([ps [k v]] (prefix ps k v)))

(defn prefix-v
  "Produce seq of `k v` pairs where each `v` is prefixed with one of `ps`."
  ([ps k v]
  (map (fn [[x y]] [y x]) (prefix ps v k)))
  ([ps [k v]] (prefix-v ps k v)))

(defn suffix-nums
  "Transform all numeric values to strings with suffix of s (%, px, em, &c.)."
  [s xs]
  (walk/postwalk #(if (number? %) (str % s) %) xs))

(defn at-rule
  "Render a css at-rule (that's what the W3C calls them) to string."
  [[x ys]]
  (with-format
      (str (name x) lb (quick-render ys) rb)))

(def xb
  "Return seq of k v pairs with key prefixed for each major browser."
  (partial prefix default-browsers))

(def xb-v
  "Return seq of k v pairs with value prefixed for each major browser."
  (partial prefix-v default-browsers))

(defn prefix-at-rule
  "Return string comprising an at-rule (q.v.) for each prefix in `ps`."
  [ps x]
  (let [ps (map #(str \@ %) ps)]
    (->> x
         (prefix ps)
         concat
         (map at-rule)
         (reduce str))))

(def at-rule-xb
  "Return string comprising an at-rule (q.v.) for each major browser."
  (partial prefix-at-rule default-browsers))

(defn auto-prefix
  "Return new selector/ruleset seq with prefixed versions as necessary for css
  properties found in `cross/*needful*`. Only as good as `*needful*` is
  accurate."
  [xs]
  (let [g (fn [x] (when (seq x)
                    (if-let [ps (cross/prop-pres (first x))]
                      (prefix ps x)
                      [x])))]
    (map (fn [[k v]] [k (mapcat g v)]) xs)))
