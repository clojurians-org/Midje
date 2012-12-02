(ns ^{:doc "In-repl user documentation"}
  midje.doc
  (:use clojure.pprint)
  (:require [midje.util.colorize :as color]
            [clojure.java.browse :as browse]
            [midje.util.ecosystem :as ecosystem]))

(def appropriate? ecosystem/running-in-repl?)
(def for-sweet '[midje-help
                 midje-configuration-help
                 print-level-help
                 guide])
(def for-repl  '[midje-repl-help])

(defn repl-notice []
  (println (color/note "Run `(midje-repl-help)` for descriptions of Midje repl functions.")))

(defn midje-notice []
  (println (color/note "For Midje usage, run `(midje-help)`."))
  (println (color/note "For Midje configuration options, run `(midje-configuration-help)`.")))


(def guide-topics {
 'future-facts  "https://github.com/marick/Midje/wiki/Future-facts"
 'partial-prerequisites "https://github.com/marick/Midje/wiki/Partial-prerequisites"
})
                    
(defmacro guide
  "Open a web page that addresses a particular topic"
  [topic]
  `(if-let [url# (guide-topics '~topic)]
     (browse/browse-url url#)
     (do 
       (println "There is no such topic. Did you mean one of these?")
       (doseq [topic# (keys guide-topics)]
         (println "   " topic#)))))
  

(defn midje-repl-help
  "Midje repl help"
  []
  (println)
  (println "Here are Midje repl functions. Use `doc` for more info.")
  (println "To control verbosity of output, use print levels defined ")
  (println "by `(print-level-help)`.")
  (println)
  (println "----- Loading facts")
  (println "You load facts by namespace. Namespace names need not be quoted.")
  (println "(load-facts <ns> <ns>...)")
  (println "(load-facts midje.util.*)       ; Load all namespaces below midje.util.")
  (println "(load-facts <ns> :integration)  ; Apply filters to facts.")
  (println)
  (println "----- Running facts")
  (println "(check-facts)                   ; in current namespace")
  (println "(check-facts <ns> <ns>...)      ; in given namespaces")
  (println "(check-facts :all)              ; all defined facts")
  (println "(check-facts :all :integration) ; all integration tests")
  (println "(check-facts \"partial\"        ; in this namespace when")
  (println "                                ; name contains \"partial\"")
  (println "There are other filter arguments.")
  (println)
  (println "----- Rerunning facts")
  (println "(recheck-fact)                ; Check just-checked fact again.")
  (println "(rcf)                         ; Synonym for above.")
  (println)
  (println "Note: facts with `:check-only-at-load-time`")
  (println "metadata do not get stored for rerunning.")
  (println)
  (println "----- Forgetting facts")
  (println "Same notation as the `check-facts` family, but with")
  (println "\"forget\" instead of \"check\"")
  (println)
  (println "----- Fetching facts")
  (println "Same notation as the `check-facts` family, but with")
  (println "\"fetch\" instead of \"check\". The return value is a")
  (println "sequence of functions. To check the corresponding facts,")
  (println "use `check-one-fact`:")
  (println "   (check-one-fact <fact-function>)")
  (println)
  (println "In addition you can fetch the last fact checked with")
  (println "`(last-fact-checked)`. `(source-of-last-fact-checked)`")
  (println "gives you its source.")
  (println)
  (println "To query fact function metadata, use these:")
  (println "-- (fact-name <ff>)                ; result might be nil")
  (println "-- (fact-source <ff>)")
  (println "-- (fact-file <ff>)")
  (println "-- (fact-line <ff>)")
  (println "-- (fact-namespace <ff>)")
  (println "-- (fact-description <ff>)         ; the doc string; might be nil")
  (println)
  (println "----- Overriding the configuration")
  (println "(midje.config/with-augmented-config")
  (println "    {:visible-deprecation false, :check-after-creation false}")
  (println "  (load-facts midje.ideas.*))")
  (println)
  (println "See `(midje-configuration-help)` for a description of the")
  (println "configuration keywords and values.")
  )

(defn print-level-help
  "Description of print levels."
  []
  (println "The `load-facts`, `check-facts`, and `recheck-fact`")
  (println "functions normally print any fact failures and a final")
  (println "summary. The detail printed can be adjusted by passing")
  (println "either certain keywords or corresponding numbers to the")
  (println "functions. (The numbers are easier to remember.)")
  (println "For example, here's how you check all facts in the most")
  (println "verbose way:")
  (println "  (check-facts :all 2)")
  (println "  (check-facts :all :print-facts)")
  (println)
  (println "Here are all the variants:")
  (println)
  (println ":print-normally     (0)  -- failures and a summary.")
  (println ":print-no-summary  (-1)  -- failures only.")
  (println ":print-nothing     (-2)  -- nothing is printed.")
  (println "                         -- (but return value can be checked)")
  (println ":print-namespaces   (1)  -- print the namespace for each group of facts.")
  (println ":print-facts        (2)  -- print fact descriptions in addition to namespaces.")
  )



(defn midje-help
  "Midje help"
  [& args]
  (if (empty? args)
    (do 
      (println "** Topics:")
      (println "* (midje-help :checkers)")
      (println "* (midje-help :prerequisites)  ; deferring coding of helper functions.")
      (println "* (midje-help :setup)          ; and teardown too")
      (println "* (midje-help :arrows)         ; variants on =>")
      (println)
      (println "** About facts")
      (println "* A common form:")
      (println "(fact ")
      (println "  (let [result (prime-ish 5)]")
      (println "    result => odd?")
      (println "    result => (roughly 13)")
      (println)
      (println "* Nested facts")
      (println "(facts \"about life\"")
      (println "  (facts \"about birth\"...)")
      (println "  (facts \"about childhood\"...)")
      (println "  ...)")
      (println)
      (println "* metadata")
      (println "(fact :integration ...)")
      (println "(fact {priority 5} ...)"))
    (doseq [topic args]
      (condp = topic
        :checkers
        (do 
          (println "** Checkers:")
          (println "(facts \"about checkers\"")
          (println "  (f) => truthy")
          (println "  (f) => falsey")
          (println "  (f) => irrelevant ; or `anything`")
          (println "  (f) => (exactly odd?) ; when function is returned")
          (println "  (f) => (roughly 10 0.1)")
          (println "  (f) => (throws SomeException \"with message\")")
          (println "  (f) => (contains [1 2 3]) ; works with strings, maps, etc.")
          (println "  (f) => (contains [1 2 3] :in-any-order :gaps-ok)")
          (println "  (f) => (just [1 2 3])")
          (println "  (f) => (has every? odd?)")
          (println "  (f) => (nine-of odd?) ; must be exactly 9 odd values.")
          (println "  (f) => (every-checker odd? (roughly 9)) ; both must be true")
          (println "  (f) => (some-checker odd? (roughly 9)) ; one must be true")
          (println))

        :prerequisites
        (do
          (println "** Prerequisites")
          (println "* Prerequisites and top-down TDD:")
          (println "(unfinished char-value chars)")
          (println "(fact \"a row value is composed of character values\"")
          (println "   (row-value ..row..) => \"53\"")
          (println "   (provided")
          (println "     (chars ..row..) => [..five.. ..three..]")
          (println "     (char-value ..five..) => \"5\"")
          (println "     (char-value ..three..) => \"3\"))")
          (println)
          (println "* Prerequisites can be defaulted for claims within a fact:")
          (println "(fact \"No one is ready until everyone is ready.\"")
          (println "  (against-background")
          (println "     (pilot-ready) => true, (copilot-ready) => true,")
          (println "     (flight-engineer-ready) => true)")
          (println)
          (println "   (ready) => truthy")
          (println "   (ready) => falsey (provided (pilot-ready) => false)")
          (println "   (ready) => falsey (provided (copilot-ready) => false)")
          (println "   (ready) => falsey (provided (flight-engineer-ready) => false)")
          (println)
          (println "* Prerequisites can also be wrapped around facts.")
          (println "(against-background [(pilot-ready) => true")
          (println "                     (copilot-ready) => true")
          (println "                     (flight-engineer-ready) => true]")
          (println "  (fact \"No one is ready until everyone is ready.\"")
          (println "    (ready) => truthy")
          (println "    (ready) => falsey (provided (pilot-ready) => false)")
          (println "    (ready) => falsey (provided (copilot-ready) => false)")
          (println "    (ready) => falsey (provided (flight-engineer-ready) => false))")
          (println))

        :setup
        (do
          (println "** Setup/Teardown")
          (println "* Around facts")
          (println "(against-background [(before :facts (do-this))")
          (println "                     (after :facts (do-that))")
          (println "                     (around :facts (wrapping-around ?form))]")
          (println "   (fact ...)")
          (println "   (fact ...)")
          (println)
          (println "* Around checks")
          (println "(against-background [(before :checks (do-this))")
          (println "                     (after :checks (do-that))")
          (println "                     (around :checks (wrapping-around ?form))]")
          (println "   (fact ...)")
          (println "   (fact ...)")
          (println)
          (println "* Setup/teardown can be placed within fact bodies")
          (println "(fact ")
          (println "  (against-background")
          (println "     (before :checks (do-this))")
          (println "     (after :checks (do-that))")
          (println "     (around :checks (wrapping-around ?form)))")
          (println "  ...)")
          (println))

        :arrows
        (do
          (println "** Arrow forms")
          (println "* Claims")
          (println "5     =not=>    even?      ; Invert the check. Synonym: =deny=>")
          (println "(f)   =future=> :halts?    ; don't check, but issue reminder.")
          (println "(m x) =expands-to=> form   ; expand macro and check result")
          (println)
          (println "* Prerequisites")
          (println "..meta.. =contains=> {:a 1} ; partial specification of map-like object")
          (println "(f)      =streams=>  (range 5 1000)")
          (println "(f)      =throws=>   (IllegalArgumentException. \"boom\")"))))))

(defn midje-configuration-help
  "What can go in the .midje.clj files."
  []
  (println "On startup, Midje loads ${HOME}/.midje.clj and ./.midje.clj")
  (println "(in that order). To affect the default configuration, use")
  (println "code like this:")
  (println "    (override-with :visible-deprecation false")
  (println "                   :visible-future false")
  (println "                   :print-level :print-namespaces)")
  (println)
  (println "You can temporarily override the configuration like this:")
  (println "    (midje.config/with-augmented-config {:print-level :print-no-summary}")
  (println "       <forms>...)")
  (println)
  (println "------ Configuration keywords")
  (println ":print-level                  ; Verbosity of printing.")
  (println "                              ; See `(print-level-help)`")
  (println)
  (println ":check-after-creation         ; Should facts be checked as they're loaded?")
  (println "                              ; Default true.")
  (println)
  (println ":visible-deprecation          ; Whether information about deprecated")
  (println "                              ; features or functions is printed.")
  (println "                              ; Default true.")
  (println)
  (println ":visible-future               ; Whether future facts produce output.")
  (println "                              ; Default true.")
  (println "                              ; More: `(guide future-facts)`")
  (println)
  (println ":partial-prerequisites        ; Whether the real function can be used.")
  (println "                              ; Default false.")
  (println "                              ; More: `(guide partial-prerequisites)`")
  (println)
  )

