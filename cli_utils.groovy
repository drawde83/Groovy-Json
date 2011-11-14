/**
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

def findArgInd = { a, cl -> a.collect(cl).findAll{it > -1}}

/** creates map from long args, with long arg as key **/
def procLongArgs = {a -> 
res = [:]
    findArgInd(a,{(it ==~ /^--.*/) ? a.indexOf(it) : -1})
    .collect{[(a[it]):(a[it + 1])]}
    .each{ res += it}
    return res
}

/** turns flags into a Set **/
def procFlags = {a ->
  return (HashSet) a.collect{it}.findAll{(it ==~ /^-[^-].*/)}
}

longArgs = [:]
flags = new java.util.HashSet();
args = args.collect{it}

res = [
  "longArgs":procLongArgs(args),
  "flags":procFlags(args)
]
