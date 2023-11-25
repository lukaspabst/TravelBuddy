import {motion} from "framer-motion";


const AnimationFlash = ({ children }) => {
    return(
        <motion.div
            key="uniqueKey"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.3 }}
        >
        {children}
    </motion.div>
    );
};
export default AnimationFlash;
