import JSONEditor from 'jsoneditor/dist/jsoneditor-minimalist';
import 'jsoneditor/dist/jsoneditor.css';
import { useEffect, useRef, useState } from 'react';
let editor;
export default function CompJSONEditor({ style, defaultJSON, onChange, ...rest }) {
    const divRef = useRef(null)

    function handleChange() {
        if (editor && onChange) {
            const currentJson = editor.get();
            onChange(currentJson);
        }
    }

    function createEditor() {
        editor = new JSONEditor(divRef.current, {
            mode: 'tree',
            onChange: handleChange,
            ...rest
        })
    }

    useEffect(() => {
        createEditor()
        defaultJSON && editor.set(defaultJSON)
        return (() => editor && editor.destroy())
    }, [])

    return (
        <div ref={divRef} style={style || {}}></div>
    )
}