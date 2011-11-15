#!/usr/bin/env groovy

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

import groovy.json.*
import java.util.HashSet

/** closures **/
def getCliAttr = {
  roots = (String[]) ["./"]
  def engine = new GroovyScriptEngine(roots)

  def binding = ['args':args] as Binding

  engine.run("cli_utils.groovy", binding)
  return binding.getVariable('res')
}

def main = {
    cli_attr = getCliAttr()
    flags = cli_attr["flags"]
    res_map = [:]
    longArgs = cli_attr["longArgs"]
    name = longArgs["--name"]
    stdin = System.in.text


    hasFlag = {flags.contains(it)}

    jSlurp = {
    	js = new JsonSlurper()
        return js.parseText(stdin)
    }

    jBuild = {rm -> 
        jb = new JsonBuilder(rm)
        return jb.toString()
    }


    if(hasFlag("-isText")){
	res_map[name] = stdin

    }else{							//default mode accepting JSON over StdIn
        res_map = jSlurp()
    }


    if(longArgs["--addVal"] != null){
	it = longArgs["--addVal"].split(",")
	res_map[it[0]] = it[1]

    }

    return jBuild(res_map)
}

println main()
