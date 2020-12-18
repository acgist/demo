import * as mod from "./test";
import modes from "./test";
import {add, say} from "./tests";

console.log(module.paths);

console.log(mod);

mod.default.call();
modes.call();
add(1, 1);
say();