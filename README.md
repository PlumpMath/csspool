# csspool

`csspool` is a css generation library designed for Clojure programmers. It handles
syntax quoted trees and encourages mixins and functions spliced in. This makes
it considerably easier to build the css structure.

The driving force behind its creation was making browser specific prefixing
automagic where possible and painless where not.

## Does the World Need Another CSS Generator

Probably not, but I did. I wanted one that worked in Clojure, had a forgiving
and manipulable structure and made cross browser pain go away. None of the libs
I could find satisfied me, as they seemed overly rigid in structure, and
structured css by mirroring Clojure data structures, which is at best awkward.
Also, although this is quite understandable,
they required all selectors, properties and values to be keywords or strings.
By operatingon  syntax quote strings `csspool` allows what appear to be barewords.

`csspool` can also prefix all properties that need it to work across browsers
automatically and efficiently -- only those prefices necessary for the property
are processed. For instance, only Mozilla and Webkit browsers support
`border-start`, so only they are output. `transform`, though, has vendor specific
identifiers for Mozilla, Webkit and Opera, while Explorer recognizes it without
prefix, so one plain and three prefixed versions make it into the final css.

## Usage

```clojure
(def styles
  `[.my-div [height 100 width 60 float left]
    ~@(let [c1 :white c2 'red c3 "#FFF"] ; depth difference fixed invisibly
        `(.his-div {color ~c1 background ~c3 outline-color ~(if (= c1 c3) c2 c3)}))
    ;; all will be prefixed as needed.
    .her-div [tab-size :40px flex :1 flex-grow :2]
    ;; initial '&' will add entire preceding selector (.her-div ul).
    &ul [color green]
    ;; (.her-div ul li)
    &li (color blue)
    ;; % will add one pirce of preceding for each % (.her-div ul:hover)
    %%:hover {background-color red}
    ])

(add-processors [suffix-px] ; add px to any naked numerals
  (with-pretty ; not minified
    (print (css styles))))
```

This produces the output that follows. Without `with-pretty` it would have been
minified. Notice that `linear-gradient` is a value, not a property, so it must
be explicitly prefixed, while `flex`, `flex-grow` and `tab-size` are handled
by `auto-prefix.` (`xb-v` means cross browser value.)

```css
.my-div{
  height: 100px;
  width: 60px;
  float: left;
}
.his-div{
  background: #FFF;
  outline-color: #FFF;
  color: white;
}
.her-div{
  tab-size: 40px;
  -webkit-tab-size: 40px;
  flex: 1;
  -webkit-flex: 1;
  -ms-flex: 1;
  flex-grow: 2;
  -webkit-flex-grow: 2;
}
.her-div ul{
  color: green;
}
.her-div ul li{
  color: blue;
}
.her-div ul:hover{
  background-color: red;
}
```
## Example

Have an [annotated, working example.](http://crooney.github.io/csspool)

## Documentation

[Sure](http://crooney.github.io/csspool/uberdoc.html). Doesn't cost me anything.

## License

Copyright © 2013 Christopher J Rooney

Distributed under the Eclipse Public License, the same as Clojure.
