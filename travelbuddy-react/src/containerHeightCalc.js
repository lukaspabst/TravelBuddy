import { useEffect, useState } from 'react';

function useContainerHeight(ref) {
    const [containerHeight, setContainerHeight] = useState(0);

    useEffect(() => {
        const updateContainerHeight = () => {
            if (ref.current) {
                setContainerHeight(ref.current.clientHeight);
            }
        };

        updateContainerHeight(); // Initialisiere die Höhe

        window.addEventListener('resize', updateContainerHeight);
        window.addEventListener('popstate', updateContainerHeight);

        return () => {
            window.removeEventListener('resize', updateContainerHeight);
            window.removeEventListener('popstate', updateContainerHeight);
        };
    }, [ref.current]);

    return containerHeight;
}

export default useContainerHeight;
