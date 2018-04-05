/**
 * Created by Dev2grow on 26/05/2017.
 */
import { Navigation } from 'react-native-navigation';

import Menu from './Containers/Menu'
import List from './Containers/List'
import Create from './Containers/Create'
import Edit from './Containers/Edit'


export function registerScreens() {
    Navigation.registerComponent('menu', () => Menu);
    Navigation.registerComponent('list', () => List);
    Navigation.registerComponent('create', () => Create);
    Navigation.registerComponent('edit', () => Edit);
}