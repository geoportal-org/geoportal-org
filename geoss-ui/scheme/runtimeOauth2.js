import Oauth2Scheme from './oauth2'

export default class RuntimeConfigurableOauth2Scheme extends Oauth2Scheme {
  constructor($auth, options) {
    const configOptions = {
      ...options,
      ...$auth.ctx.$config.auth.strategies[options['_name']]
    };
    super($auth, configOptions);
  }
}
