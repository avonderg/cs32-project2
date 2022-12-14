// Look up the Web Speech API (https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis)
// Initialize this variable when the window first loads
let VOICE_SYNTH: SpeechSynthesis;

// The current speaking rate of the screen reader
let VOICE_RATE: number = 1;

// Stores elements and their handler functions
// Think of an appropriate data structure to do this
// Assign this variable in mapPage()
let ELEMENT_HANDLERS: {[key: number]: [HTMLElement, (arg0: HTMLElement) =>  Promise<void>]} = {};

// Indicates the current element that the user is on
// You can decide the type of this variable
let current: string;
let prev: string // ID of previous

/**
 * Speaks out text.
 * @param text the text to speak
 */
function speak(text: string) {
    if (VOICE_SYNTH) {
        // initialize a speech request using SpeechSynthesisUtterance
        let utterance = new SpeechSynthesisUtterance(text);
        utterance.rate = VOICE_RATE;

        // listens for pause
        utterance.addEventListener('pause', function(event) {
            console.log('Speech paused after ' + event.elapsedTime + ' milliseconds.');
        });

        VOICE_SYNTH.speak(utterance);

        // begins to speak

        console.log("Rate: " + utterance.rate)
        return new Promise<void>((resolve) => {
            utterance.onend = () => resolve()
        })
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

    // screen reader button events
    const buttons = document.getElementById("screenReader")!.getElementsByTagName("button");

    buttons[0]!.addEventListener("click", (event) => start("0"));
    buttons[1]!.addEventListener("click", function(){
        if (VOICE_SYNTH.paused) { resume(); }
        else { pause(); }
    });
    buttons[2]!.addEventListener("click", (event) => changeVoiceRate(1.1));
    buttons[3]!.addEventListener("click", (event) => changeVoiceRate(0.1));

    document.addEventListener("keydown", globalKeystrokes);
}

/**
 * Gets all the elements by their tagnames, sets their ID's, and inserts each element into the global
 * ELEMENT_HANDLERS array along with their handler functions
 */
function generateHandlers(): void {
    // gets HTML elements
    const collection : HTMLCollectionOf<Element> = document.getElementsByTagName("*");

    // iterate through all elements in DOM
    let i = 0;
    for (let e of collection as any) {

        const htmlElt = e as HTMLElement
        let textTags: Array<string> = ["P", "H1", "H2", "H3", "H4", "H5", "H6", "LABEL", "TITLE", "CAPTION", "TH", "TD"]
        let tableTags: Array<string> = ["TABLE", "CAPTION", "TD", "TFOOT", "TH", "TR"];

        // store elements and associated handlers in ELEMENT_HANDLERS
        if (textTags.indexOf(htmlElt.tagName) > -1) {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => pureTextHandlers(x)]
        } else if (htmlElt.tagName == "IMG") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => imgHandlers(x)]
        } else if (htmlElt.tagName == "A") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => linkHandlers(x)]
        } else if (htmlElt.tagName == "INPUT") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => inputHandlers(x)]
        }
        else if (htmlElt.tagName == "BUTTON") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => buttonHandlers(x)]
        }
        else if (tableTags.indexOf(htmlElt.tagName) > -1) {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => tableHandlers(x)]
        } else {
            continue;
        }

        // assign element an id
        htmlElt.id = String(i);
        i = i+1
    }
}

/**
 * Generates handler functions for text elements
 * @param elt: HTMLElement input
 */
async function pureTextHandlers(elt : HTMLElement): Promise<void> {
    if (elt.tagName == "TITLE") {
        await speak("Title " + (elt.textContent as string))
    } else if (elt.tagName == "LABEL"){
        await speak("Label " + (elt.textContent as string))
    } else {
        await speak((elt.textContent as string))
    }
}

/**
 * Generates handler functions for image elements
 * @param e: HTMLElement input
 */
async function imgHandlers(e: HTMLElement): Promise<void> {
    if ((e as HTMLImageElement).alt != "") {
        await speak("This is a picture of " + (e as HTMLImageElement).alt as string)
    } else {
        await speak("There is a picture here")
    }
}

/**
 * Generates handler functions for input elements
 * @param elt: HTMLElement input
 */
async function inputHandlers(elt: HTMLElement): Promise<void> {
    let type = (elt as HTMLInputElement).type

    document.body.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            // Cancel the default action, if needed
            event.preventDefault();

            // trigger button element
            elt.focus();
            // differentiate between different types of inputs
            if (type == "text") { // non-button input element
                let e = document.getElementById(current)
                if (e != null) {
                    if (e.onfocus) {
                        e.blur()
                    }
                    else {
                        e.focus()
                    }
                }
            }
            else if (type == "submit") // submit button
            {
                document.getElementById(current)!.click();
            }

            document.getElementById(current)!.click();
        }
    });

    VOICE_SYNTH.cancel()

    await speak("There is an input of type \""
        + (elt as HTMLInputElement).type +" \" here. Click enter to interact with it. Click escape to resume")

    return new Promise<void>((resolve) => {
        document.body.addEventListener("keyup", function(event) {

            if (event.key === "Escape") {
                // Cancel the default action, if needed
                event.preventDefault();

                VOICE_SYNTH.cancel();
                // Trigger the button element with a click
                resolve();
            }
        });
    })
}

/**
 * Generates handler functions for button elements
 * @param elt
 */
async function buttonHandlers(elt: HTMLElement): Promise<void> {
    let button = elt as HTMLButtonElement

    document.body.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            // Cancel the default action, if needed
            event.preventDefault();

            document.getElementById(current)!.click();
        }
    });

    VOICE_SYNTH.cancel()

    await speak("There is a Button here that says " + (button.value || "nothing") +
        ". Click enter to interact with it. " +  "Click escape to resume.")

    return new Promise<void>((resolve) => {
        document.body.addEventListener("keyup", function(event) {
            // Number 13 is the "Enter" key on the keyboard
            if (event.key === "Escape") {
                // Cancel the default action, if needed
                event.preventDefault();

                // cancel the current utterance and continue the readings
                console.log("RESOLVED");
                VOICE_SYNTH.cancel();
                resolve();
            }
        });
    })

}

/**
 * Generates handler functions for link elements
 * @param elt: HTMLElement input
 */
async function linkHandlers(elt: HTMLElement): Promise<void> {
    document.body.addEventListener("keyup", function(event) {
        if (event.key === "Enter") {
            // Cancel the default action, if needed
            event.preventDefault();
            // Trigger the button element with a click
            window.open((elt as HTMLLinkElement).href);
        }
    });
    await speak(elt.textContent + ". There is a link here. Click enter to enter the link. Click escape to resume")

    return new Promise<void>((resolve) => {
        document.body.addEventListener("keyup", function(event) {
            if (event.key === "Escape") {
                // Cancel the default action, if needed
                event.preventDefault();

                // cancel the current utterance and continue the readings
                console.log("RESOLVED");
                VOICE_SYNTH.cancel();
                resolve();
            }
        });
    })
}

/**
 * Generates handler functions for table elements
 * @param elt:  HTMLElement input
 */
async function tableHandlers(elt: HTMLElement): Promise<void> {
    if (elt.tagName == "CAPTION" || elt.tagName == "TH" || elt.tagName === "TD"){
        if (elt.children.length<0) {
            await speak(elt.textContent as string)
        }
    }
}

/**
 * Function highlighting input HTML elements, to help partially-blind users recognize which section
 * is currently being read by the screen reader. Highlights, and unhighlights, current and prev elements respectfully
 * @param elt - current element
 */
async function highlight(elt: Element): Promise<void>{

    const prevElt = document.getElementById(prev);

    // resets prev element's background color
    if ( prevElt != null) {
        // prevElt.style.background = document.body.style.backgroundColor || "#0000ffff";
        prevElt.style.background = document.body.style.backgroundColor;
    }

    const curr = document.getElementById(elt.id);
    if( curr != null) {
        curr.style.background = "#fff8a6";
    }

}

/**
 * Changes the speaking rate of the screen reader.
 * @param factor multiplier on the speaking rate
 */
function changeVoiceRate(factor: number): void {
    VOICE_RATE *= factor
    if (VOICE_RATE > 4) {
        VOICE_RATE = 4;
    } else if (VOICE_RATE < 0.25){
        VOICE_RATE = 0.25;
    }
}

/**
 * Moves to the next HTML element in the DOM.
 */
async function next() {
    console.log("TO NEXT");
    VOICE_SYNTH.cancel();
}

/**
 * Moves to the previous HTML element in the DOM.
 */
async function previous() {
    console.log("TO PREVIOUS")
    VOICE_SYNTH.cancel();

    if (ELEMENT_HANDLERS[+prev-2] != null) {
        prev = String(+prev-2)
    }
    if (ELEMENT_HANDLERS[+current-2] != null) {
        VOICE_SYNTH.cancel();
        // @ts-ignore
        document.getElementById(current).style.background = document.body.style.backgroundColor;
        current = String(+current-2)
    }
}

/**
 * Starts reading the page continuously.
 */
async function start(curr: String) {
    console.log('Start');
    let currentElement =  ELEMENT_HANDLERS[+current]

    if (currentElement != null) {
        console.log("ON " + current)
        await highlight(currentElement[0])
        await currentElement[1](currentElement[0])
        prev = current
        current = String(+current+1)
        await start(current)
    }

    console.log('End');
}

/**
 * Pauses the reading of the page.
 */
 function pause() {
     VOICE_SYNTH.pause();
}

 /**
 * Resumes the reading of the page.
 */
  function resume() {
      VOICE_SYNTH.resume();
 }

/**
 * Listens for keydown events.
 * @param event keydown event
 */
function globalKeystrokes(event: KeyboardEvent): void {
    // can change and add key mappings as needed
    if (event.key === " ") {
        event.preventDefault();
        current = "0"
        start("0");
    } else if (event.key === "ArrowRight") {
        event.preventDefault();
        changeVoiceRate(1.1);
    } else if (event.key === "ArrowLeft") {
        event.preventDefault();
        changeVoiceRate(0.9);
    }
    else if (event.key === "p") {
        console.log(VOICE_SYNTH.paused);
        if (VOICE_SYNTH.paused) {  // if it is paused, then resume
            resume();
        }
        else {  // otherwise, pause
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
