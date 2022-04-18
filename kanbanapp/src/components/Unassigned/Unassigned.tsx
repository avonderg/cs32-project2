import React, {useState} from 'react';


type TableValues = {
    text: string[]
}

function Unassigned(props: TableValues) {
    return(
        <tr>
            <td></td>
            <td>{props.text[0]}<br></br>{props.text[1]}<br></br>{props.text[2]}<br></br>{props.text[3]}<br></br></td>
            <td></td>
            <td></td>
        </tr>
    )
}

export default Unassigned