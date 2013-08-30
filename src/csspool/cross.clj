(ns csspool.cross)

(def browsers
  "Map of keyword to browser prefix where `:n` -> none, `:m` -> Firefox,
  `:o` -> Opera, `:w` -> webkit family, and, least, `:i` -> Internet Explorer."
  {:n nil :m "-moz-" :w "-webkit-" :o "-o-" :i "-ms-"})

(def default-browsers (vals browsers))

(defn xb-pres
  "Return seq of browser prefices mapped from each b, or nil. See `browsers`."
  [bs]
  (seq (map browsers bs)))

(def ^:dynamic *needful*
  "Map of css property to seq of `browsers` keys they require."
  {
   :animation [:n :m :o :w]
   :animation-delay [:n :m :o :w]
   :animation-direction [:n :m :o :w]
   :animation-duration [:n :m :o :w]
   :animation-fill-monde [:n :m :o :w]
   :animation-iteration-count [:n :m :o :w]
   :animation-name [:n :m :o :w]
   :animation-play-state [:n :m :o :w]
   :animation-timing-function [:n :m :o :w]
   :appearance [:m :w]
   :backface-visibility [:n :w]
   :background-clip [:n :w]
   :background-origin [:n :w]
   :background-size [:n :w]
   :border-bottom-left-radius [:n :w]
   :border-bottom-right-radius [:n :w]
   :border-end [:n :w]
   :border-end-color [:m :w]
   :border-end-style [:m :w]
   :border-end-width [:m :w]
   :border-image [:n :w :o]
   :border-radius [:n :w]
   :border-start [:m :w]
   :border-start-color [:m :w]
   :border-start-style [:m :w]
   :border-start-width [:m :w]
   :border-top-left-radius [:n :w]
   :border-top-right-radius [:n :w]
   :box-align [:m :w]
   :box-direction [:m :w]
   :box-flex [:m :w]
   :box-ordinal-group [:m :w]
   :box-orient [:m :w]
   :box-pack [:m :w]
   :box-shadow [:n :w]
   :box-sizing [:n :m :w]
   :column-count [:n :m :w]
   :column-fill [:n :m :w]
   :column-gap [:n :m :w]
   :column-rule [:n :m :w]
   :column-rule-color [:n :m :w]
   :column-rule-style [:n :m :w]
   :column-rule-width [:n :m :w]
   :column-width [:n :m :w]
   :columns [:n :m :w]
   :filter [:n :w :i]
   :flex [:n :w :i]
   :flex-basis [:n :w :i]
   :flex-direction [:n :w :i]
   :flex-grow [:n :w]
   :flex-shrink [:n :w :i]
   :font-feature-settings [:n :m :w]
   :font-kerning [:n :w]
   :font-variant-ligatures [:n :w]
   :justify-content [:n :w]
   :margin-end [:m :w]
   :margin-start [:m :w]
   :mask [:n :w]
   :order [:n :w]
   :overflow-x [:n :i]
   :overflow-y [:n :i]
   :padding-end [:m :w]
   :padding-start [:m :w]
   :perspective [:n :w]
   :perspective-origin [:n :w]
   :tab-size [:n :w]
   :text-align-last [:m :w :i]
   :text-decoration [:n :w]
   :text-decoration-color [:m :w]
   :text-decoration-line [:m :w]
   :text-decoration-style [:m :w]
   :transform [:n :m :w :o]
   :transform-origin [:n :w :o]
   :transform-style [:n :w]
   :transition [:n :w :o]
   :transition-delay [:n :w :o]
   :transition-duration [:n :w :o]
   :transition-property [:n :w :o]
   :transition-timing-function [:n :w :o]
   :user-modify [:m :w]
   :user-select [:m :w :i]
   })

(defn- pres-needed
  [p]
  (when (or (keyword? p) (symbol? p) (string? p))
    ((keyword (name p)) *needful*)))

(defn prop-pres
  "Return seq of prefices needed for css property `p`."
  [p]
  (xb-pres (pres-needed p)))
