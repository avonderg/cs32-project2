import React, {useState} from 'react';


type TableValues = {
    text: string[]
}

function InProgress(props: TableValues) {
    return(
        <tr>
            <td></td>
            <td></td>
            <td><strong>ID: </strong>{props.text[0]}<br></br><strong>Title: </strong>{props.text[1]}<br></br><strong>Task: </strong>{props.text[2]}<br></br><strong>Date: </strong>{props.text[3]}<br></br></td>
            <td></td>
        </tr>
    )
}

export default InProgress