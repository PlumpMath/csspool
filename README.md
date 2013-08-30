# csspool

`csspool` is a css generation library designed for Clojure programmers. It handles
syntax quoted trees and encourages mixins and functions spliced in. This makes
it considerably easier to build the css structure.

The driving force behind its creation was making browser specific prefixing
automagic where possible and painless where not.

## Does the World Need Another CSS Generator

Probably not, but I did. I wanted one that worked in Clojure, had a forgiving
and manipulable structure and made cross browser pain go away. None of the libs
I could find satisfied me, as they seemed overly rigid in structure, mandating
map here but vector here, etc. Also, although this is quite understandable,
they required all selectors, properties and values to be keywords or strings.
By handling syntax quote strings `csspool` allows what appear to be barewords.

`csspool` can also prefix all properties that need it to work across browsers
automatically and efficiently -- only those prefices necessary for the property
are processed. For instance, only Mozilla and Webkit browsers support
`border-start`, so only they are output. `transform`, though, has vendor specific
identifiers for Mozilla, Webkit and Opera, while Explorer recognizes it without
prefix, so one plain and three prefixed versions make it into the final css.

## Usage

```clojure
(def styles
  [;; simple
   '(.my-div [height 100 width 60 float left])
   ;; throw in some mixins, and use a map this time
   (let [c1 :white c2 'red c3 "#FFF"]
     `(.his-div {color ~c1 background ~c3 outline-color ~(if (= c1 c3) c2 c3)}))
   ;; let's prefix
   `(.her-div [tab-size :40px flex :1 flex-grow :2
               ~@(xb-v '(background-image "linear-gradient(left,blue,red)"))])])

(with-pretty
  (print (->> styles
              prepare          ; regularize for processing
              (suffix-nums \%) ; any naked numerals are made percentages
              auto-prefix      ; on the tin
              render)))        ; spit a string
```

This produces the output that follows. Without `with-pretty` it would have been
minified. Notice that `linear-gradient` is a value, not a property, so it must
be explicitly prefixed, while `flex`, `flex-grow` and `tab-size` are handled
by `auto-prefix.` (`xb-v` means cross browser value.)

```css
.my-div{
  height: 100%;
  width: 60%;
  float: left;
}
.his-div{
  outline-color: #FFF;
  color: white;
  background: #FFF;
}
.her-div{
  tab-size: 40px;
  -webkit-tab-size: 40px;
  flex: 1;
  -webkit-flex: 1;
  -ms-flex: 1;
  flex-grow: 2;
  -webkit-flex-grow: 2;
  background-image: -moz-linear-gradient(left,blue,red);
  background-image: -ms-linear-gradient(left,blue,red);
  background-image: linear-gradient(left,blue,red);
  background-image: -o-linear-gradient(left,blue,red);
  background-image: -webkit-linear-gradient(left,blue,red);
}
```
## Example

See <https://github.io/crooney/csspool> for an annotated, working example.

## Documentation

[Sure](https://github.io/FIXME). Doesn't cost me anything.

## License

Copyright © 2013 Christopher J Rooney

Distributed under the Eclipse Public License, the same as Clojure.
