# Bonafide

> bona fide _adjective_

> authentic, genuine, real, true, actual, sterling, sound, legal, legitimate, lawful, **valid**


Bonafide is a simple validation library for hashmap like objects in clojure.

## Example

    (def validate (comp
                       (validate-present :email)
                       (validate-format :email)))

    (validate {:email ""})

    =>

    {:email "" :errors {:email ["Not valid", "Cannot be blank"]}}

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 FIXME
