"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
// Look up the Web Speech API (https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis)
// Initialize this variable when the window first loads
let VOICE_SYNTH;
// The current speaking rate of the screen reader
let VOICE_RATE = 1;
// Stores elements and their handler functions
// Think of an appropriate data structure to do this
// Assign this variable in mapPage()
let ELEMENT_HANDLERS;
// Indicates the current element that the user is on
// You can decide the type of this variable
let current; // corresponds to ID of the element
let prev;
let PAUSED = false;
/**
 * Class for creating an element handler data structure, allowing HTML elements to be mapped to their handler
 * functions
 */
class elementHandler {
    constructor(elt, handler) {
        this.elt = elt;
        this.handler = handler;
    }
}
/**
 * Speaks out text.
 * @param text the text to speak
 */
function speak(text) {
    if (VOICE_SYNTH) {
        // initialize a speech request using SpeechSynthesisUtterance
        var utterance = new SpeechSynthesisUtterance(text);
        utterance.rate = VOICE_RATE;
        VOICE_SYNTH.speak(utterance);
        // check if it is speaking
        //
        // utterance.onstart = async function () {
        //     await new Promise<void>((resolve) => {
        //         if(utterance.onend && !PAUSED) {
        //             resolve();
        //         }
        //     });
        //     return;
        // }
        // onkeydown = async function (event : KeyboardEvent) {
        //     globalKeystrokes(event);
        // }
        // calls async func and waits for onend - if called several times
        utterance.onstart = function () {
            return __awaiter(this, void 0, void 0, function* () {
                yield utterance.onend;
                return;
            });
        };
    }
}
window.onload = () => {
    generateHandlers();
    document.body.innerHTML = `
        <div id="screenReader">
            <button>Start [Space]</button>
            <button>Pause/Resume [P]</button>
            <button onclick="changeVoiceRate(1.1);">Speed Up [Right Arrow]</button>
            <button onclick="changeVoiceRate(0.9);">Slow Down [Left Arrow]</button>
        </div>
    ` + document.body.innerHTML;
    VOICE_SYNTH = window.speechSynthesis;
    document.addEventListener("keydown", globalKeystrokes);
};
/**
 * Gets all the elements by their tagnames, sets their ID's, and inserts each element into the global
 * ELEMENT_HANDLERS array along with their handler functions
 */
function generateHandlers() {
    const collection = document.getElementsByTagName("*");
    let count = 0;
    for (let elt of collection) {
        elt.setAttribute('id', count.toString()); // sets unique numerical ID
        count += 1;
    }
    ELEMENT_HANDLERS = Array();
    for (let elt of collection) {
        ELEMENT_HANDLERS.push(new elementHandler(elt, handlers(elt)));
    }
}
/**
 * Function highlighting input HTML elements, to help partially-blind users recognize which section
 * is currently being read by the screen reader
 * @param text - text to highlight
 * @param elt - current element
 */
function highlight(text, elt) {
    // var inputText = elt.innerHTML
    //
    // var index = inputText.indexOf(text);
    // if (index >= 0) {
    //     inputText = inputText.substring(0, index) + "<span class='highlight'>" + inputText.substring(index, index + text.length) + "</span>" + inputText.substring(index + text.length);
    //     elt.innerHTML = inputText;
    // }
    var element = elt;
    element.style.backgroundColor = '2px solid yellow';
    return;
}
/**
 * Helper function generating handler functions for each of the different HTML elements being handled
 * @param elt
 */
function handlers(elt) {
    if (elt.tagName === "TITLE") { // metadata
        return function () {
            speak("Title");
            highlight(elt.innerHTML, elt); // highlight element
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "H1" || elt.tagName === "H2" || elt.tagName === "H3"
        || elt.tagName === "H4" || elt.tagName === "H5" || elt.tagName === "H6" || elt.tagName === "P") { //text
        return function () {
            if (elt.tagName != "P") {
                speak("Header");
            }
            highlight(elt.innerHTML, elt); // highlight element
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "IMG") { // graphics
        var imageElt = elt;
        return function () {
            speak("Graphic:");
            if (imageElt.alt != null) { // if there is a caption
                highlight(imageElt.alt, imageElt);
                speak(imageElt.alt);
            }
        };
    }
    else if (elt.tagName === "A") { // interactive
        return function () {
            speak("URL:");
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "LABEL") { // interactive : reads out label for an input form
        return function () {
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "INPUT") { // interactive
        return function () {
            speak("Input:");
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "BUTTON") { // interactive
        return function () {
            speak("Button:");
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "TABLE") { // tables
        var tableElt = elt;
        return function () {
            speak("Table:");
            highlight(elt.innerHTML, elt); // TODO: fix how tables are highlighted, and non-text objects
        };
    }
    else if (elt.tagName === "CAPTION") { // tables
        return function () {
            speak("Caption:");
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "TD") { // tables
        return function () {
            highlight(elt.innerHTML, elt);
            // speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "TFOOT") { // tables
        return function () {
            speak("Summarizing column:"); // TODO: fix how it is announced
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "TH") { // tables
        return function () {
            speak("Header:");
            highlight(elt.innerHTML, elt);
            speak(elt.textContent || "");
        };
    }
    else if (elt.tagName === "TR") { // tables
        return function () {
            speak("Row:");
            highlight(elt.innerHTML, elt);
            // speak(elt.textContent || "");
        };
    }
    return function () { };
}
/**
 * Changes the speaking rate of the screen reader.
 * @param factor multiplier on the speaking rate
 */
function changeVoiceRate(factor) {
    VOICE_RATE *= factor;
    if (VOICE_RATE > 4) {
        VOICE_RATE = 4;
    }
    else if (VOICE_RATE < 0.25) {
        VOICE_RATE = 0.25;
    }
}
/**
 * Moves to the next HTML element in the DOM.
 */
function next() {
    var elt = ELEMENT_HANDLERS.shift();
    if (elt != null) {
        current = elt.elt.id;
        elt.handler();
        prev = elt;
        next();
    }
}
/**
 * Moves to the previous HTML element in the DOM.
 */
function previous() {
    ELEMENT_HANDLERS.unshift(prev);
    current = prev.elt.id;
    prev.handler();
    next();
}
/**
 * Starts reading the page continuously.
 */
function start() {
    if (PAUSED) {
        VOICE_SYNTH.resume();
    }
    else {
        var elt = ELEMENT_HANDLERS.shift();
        if (elt != null) {
            current = elt.elt.id;
            elt.handler(); // calls function to start speaking
            prev = elt;
            next();
        }
    }
}
/**
 * Pauses the reading of the page.
 */
function pause() {
    PAUSED = true;
    VOICE_SYNTH.pause();
}
/**
* Resumes the reading of the page.
*/
function resume() {
    PAUSED = false;
    VOICE_SYNTH.resume();
}
/**
 * Listens for keydown events.
 * @param event keydown event
 */
function globalKeystrokes(event) {
    // can change and add key mappings as needed
    if (event.key === " ") {
        event.preventDefault();
        //TODO: start reading the entire page
        PAUSED = false;
        start();
    }
    else if (event.key === "ArrowRight") {
        event.preventDefault();
        changeVoiceRate(1.1);
    }
    else if (event.key === "ArrowLeft") {
        event.preventDefault();
        changeVoiceRate(0.9);
    }
    else if (event.key === "r") {
        if (PAUSED) { // if it is paused
            PAUSED = false;
            resume();
        }
    }
    else if (event.key === "p") {
        if (!PAUSED) { // if it is not paused
            PAUSED = true;
            pause();
        }
    }
    else if (event.key == "ArrowUp") {
        previous();
    }
    else if (event.key == "ArrowDown") {
        next();
    }
}
