import { AppVueObj } from '@/data/global';
import { GeneralActions } from '@/stores/general/general-actions';

export default {
	bind(el) {
		el.__vueImagePreview__ = {
			handler: (event: Event) => {
				const target = (event.target as HTMLImageElement);
				if(target.getAttribute('src')) {
					AppVueObj.app.$store.dispatch(GeneralActions.setImagePreview, target);
				}
			}
		};
		el.style.cursor = 'pointer';
		el.addEventListener('click', el.__vueImagePreview__.handler);
	},

	unbind(el) {
		el.style.cursor = '';
		document.removeEventListener('click', el.__vueImagePreview__.handler);
		delete el.__vueImagePreview__;
	}
};