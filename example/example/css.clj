;;; The cylon eye css which is adapted here originally comes from:
;;; https://developer.mozilla.org/en-US/docs/Web/CSS/animation

(ns example.css (:require [csspool.core :as c]))

(def styles
  (let [w :white]
  `[ ;; the raw number 100 will be dealt with later
    .view_port [background-color black height :25px width 100 overflow hidden
                ;; border-radius will be automatically prefixed for webkit
                ;; (works in FF without prefix)
                border-radius :10px]
    ;; escape the mixin w and ignore raw numbers.
    .cylon_eye [color ~w height 100 width 20 background-color red
                ;; animation will be prefixed for webkit, mozilla, and opera
                animation "move_eye 3s linear 0s infinite alternate"
                ;; background-image is a value, not a property, so it has to be
                ;; explicitly prefixed. (xb-v = cross-browser-value).
                ;; generally, the right thing is done wrt tree-depth, so it
                ;; wouldn't matter if we forgot the @.
                ~@(c/xb-v '(background-image "linear-gradient(left, rgba(0,0,0,0.9) 25%, rgba( 0,0,0,0.1 ) 50%, rgba( 0,0,0,0.9 ) 75%)"))]
     ;; maps are fine
   .polling_message {color ~w float left margin-right 2 margin-left 10 margin-top :3px}
 ]))

(def at-styles
  ;; at-styles, as the W3C calls them, are handled differently from css.
  ;; media queries go here as well.
  `("keyframes move_eye" (from (margin-left :-20%) to (margin-left :100%))))

(defn -main [& args]
  (c/with-pretty ; not overly pretty. newlines, anyway.
    (print (->> styles
                ;; regularize for processing. arbitrary levels of depth are flattened.
                c/prepare
                ;; all raw numbers converted to strings ending in \%
                (c/suffix-nums \%)
                ;; handle animation and border-radius from above as well as many
                ;; more. only adds prefices necessary and known to work. (well ...)
                c/auto-prefix
                ;; spit it out
                c/render)
           ;; have the at-rules work with all browsers
           (c/at-rule-xb at-styles))))
;;; ces't tout
