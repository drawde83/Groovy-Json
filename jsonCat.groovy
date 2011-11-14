/**
    jsonCat merges JSON docs on command-line
    Copyright (C) 2011 Edward White

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
**/

#!/usr/bin/env groovy

import groovy.json.*
import java.util.HashSet

/** closures **/
def getCliAttr = {
  roots = (String[]) ["/home/ejtrw1/"]
  def engine = new GroovyScriptEngine(roots)

  def binding = ['args':args] as Binding

  engine.run("cli_utils.groovy", binding)
  return binding.getVariable('res')
}

/**
usage:

program is designed to create a json document with data from Standard Input sent as either text or json

flag: a argument of the form -flagname, allows the program to change into diff modes.
long argument: argument of the form --name, followed by a value that the program can access.


examples:

adding key & val to doc
cat example.json | groovy jsonCat.groovy --addVal "text,some_random_text"

create json from text
cat example.txt | groovy jsonCat.groovy --name blah -isText
**/

def main = {
    cli_attr = getCliAttr()
    flags = cli_attr["flags"]
    hasFlag = {flags.contains(it)}

    res_map = [:]
    longArgs = cli_attr["longArgs"]
    name = longArgs["--name"]
    stdin = System.in.text


    if(hasFlag("-isText")){
	res_map[name] = stdin

    }else{							//default mode accepting JSON over StdIn
    	js = new JsonSlurper()
        res_map = js.parseText(stdin)

    }


    if(longArgs["--addVal"] != null){
	it = longArgs["--addVal"].split(",")
	res_map[it[0]] = it[1]

    }


    def jb = new JsonBuilder(res_map)
    println jb.toString()
}

//main(args.collect{it})
main()
