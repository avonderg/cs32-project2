import React, {useState} from 'react';


type TableValues = {
    text: string[]
}

function Completed(props: TableValues) {
    return(
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>{props.text[0]}<br></br>{props.text[1]}<br></br>{props.text[2]}<br></br>{props.text[3]}<br></br></td>
        </tr>
    )
}

export default Completed