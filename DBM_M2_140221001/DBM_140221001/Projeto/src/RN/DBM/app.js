/**
 * Created by Dev2grow on 26/05/2017.
 */
import { Navigation } from 'react-native-navigation';
import { registerScreens } from './screens';

registerScreens();

Navigation.startSingleScreenApp({
    screen: {
        screen: 'menu',
        title: 'Home',
        navigatorStyle: {
            navBarHidden: true,
        },

    },
    animationType: 'fade'
});