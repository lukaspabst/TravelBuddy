// useContainerHeight.js
import { useEffect, useState } from 'react';

function useContainerHeight(ref) {
    const [containerHeight, setContainerHeight] = useState(0);

    const updateContainerHeight = () => {
        if (ref.current) {
            setContainerHeight(ref.current.clientHeight);
        }
    };

    useEffect(() => {
        updateContainerHeight(); // Initialisiere die Höhe

        const handleResize = () => {
            updateContainerHeight();
        };

        window.addEventListener('resize', handleResize);
        window.addEventListener('popstate', updateContainerHeight);

        return () => {
            window.removeEventListener('resize', handleResize);
            window.removeEventListener('popstate', updateContainerHeight);
        };
    }, [ref.current]);

    const updateHeight = () => {
        updateContainerHeight();
    };

    return { containerHeight, updateHeight };
}

export default useContainerHeight;
