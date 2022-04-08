import 'package:flutter/widgets.dart';

/// service for the navigation inside the app
class NavigationService {
  final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  /// pushes a new view onto the stack
  Future<dynamic> pushNamed({required String routeName}) {
    return navigatorKey.currentState!.pushNamed(routeName);
  }

//pushes a new root view
  Future<dynamic> pushReplacementNamed({required String routeName}) {
    return navigatorKey.currentState!.pushReplacementNamed(routeName);
  }

  /// pops a view from the stack
  goBack() {
    return navigatorKey.currentState!.pop(navigatorKey.currentContext);
  }

  /// remove all views from the stack an sets a new one
  Future<dynamic> pushNamedAndRemoveUntil(
      {required String routeName, dynamic args}) {
    return navigatorKey.currentState!
        .pushNamedAndRemoveUntil(routeName, (_) => false, arguments: args);
  }
}
